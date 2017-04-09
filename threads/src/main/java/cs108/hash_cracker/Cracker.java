package cs108.hash_cracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by ilyarudyak on 4/7/17.
 */
public class Cracker {

    private static final Integer CRACKER_THREADS = 8;
    private static final Integer PASSWORD_LENGTH = 2;
    static final List<Character> CHARS = new CopyOnWriteArrayList<>(
            "abcdefghijklmnopqrstuvwxyz0123456789.,-!"
            .chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList())
    );
    private static CountDownLatch endGate;

    public Cracker() {
        endGate = new CountDownLatch(CRACKER_THREADS);
    }

    public String getHash(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            return hexToString(md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public byte[] getHashBytes(Character ... characters) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            return md.digest(new String(convertToChars(characters)).getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private char[] convertToChars(Character[] characters) {
        char[] chars = new char[characters.length];
        for (int i = 0; i < characters.length; ++i) {
            chars[i] = characters[i].charValue();
        }
        return chars;
    }

    public void crackPass(String hexStr) throws InterruptedException {
        byte[] passHash = hexToArray(hexStr);
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        int step = CHARS.size() / CRACKER_THREADS;
        for (int i = 0; i < CRACKER_THREADS; ++i) // create and start threads
            executor.execute(new CrackerWorker(i * step, (i + 1) * step, passHash));

        executor.shutdown();
        endGate.await();
        System.out.println("all done");
    }

    /*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
    */
    private static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i=0; i<bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val<16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    /*
     Given a string of hex byte values such as "24a26f", creates
     a byte[] array of those values, one byte value -128..127
     for each 2 chars.
     (provided code)
    */
    private static byte[] hexToArray(String hex) {
        byte[] result = new byte[hex.length()/2];
        for (int i=0; i<hex.length(); i+=2) {
            result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
        }
        return result;
    }

    private class CrackerWorker implements Runnable {

        private final List<Character> chars;
        private final byte[] passHash;

        public CrackerWorker(Integer fromIndex, Integer toIndex, byte[] passHash) {
            chars = CHARS.subList(fromIndex, toIndex);
            this.passHash = passHash;
        }

        public void run() {
            crackPassword();
            endGate.countDown();
        }

        public void crackPassword() {
            while (true) {
                for (Character ch: chars) {
                    for (Character ch2: CHARS) {
                        if (Arrays.equals(getHashBytes(ch, ch2), passHash)) {
                            System.out.println(ch + "" + ch2);
                            endGate.countDown();
                            break;
                        }
                    }
                }
            }
        }
    }

    // possible test values:
    // a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
    // fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
    // a! 34800e15707fae815d7c90d49de44aca97e2d759
    // xyz 66b27417d37e024c46526c2f6d358a754fc552f3
}
