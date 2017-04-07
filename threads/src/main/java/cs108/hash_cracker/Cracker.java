package cs108.hash_cracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by ilyarudyak on 4/7/17.
 */
public class Cracker {
    // Array of chars used to produce strings
    static final List<Character> CHARS = new CopyOnWriteArrayList<>(
            "abcdefghijklmnopqrstuvwxyz0123456789.,-!"
            .chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList())
    );

    public String getHash(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            return hexToString(md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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

    // possible test values:
    // a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
    // fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
    // a! 34800e15707fae815d7c90d49de44aca97e2d759
    // xyz 66b27417d37e024c46526c2f6d358a754fc552f3
}
