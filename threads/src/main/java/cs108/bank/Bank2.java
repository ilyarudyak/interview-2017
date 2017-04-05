package cs108.bank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class Bank2 {

    private static Integer BANK_ACCOUNTS = 20;
    private static Integer INITIAL_BALANCE = 1000;
    private static Integer CAPACITY = 10;
    private static Integer TRANS_THREADS = 5;

    private List<Account> accounts;
    private List<Transaction> transactions;
    private BlockingQueue<Transaction> queue;
    private CountDownLatch latch = new CountDownLatch(TRANS_THREADS);

    public Bank2(String fileName) {
        accounts = new CopyOnWriteArrayList(Stream.iterate(0, n -> n + 1)
                .limit(BANK_ACCOUNTS)
                .map(n -> new Account(n, INITIAL_BALANCE))
                .collect(Collectors.toList())
        );
        try {
            transactions = new CopyOnWriteArrayList(Files.lines(Paths.get(fileName))
                    .map(line -> new Transaction(line))
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        queue = new ArrayBlockingQueue<>(CAPACITY);
    }

    public void processTrans() {

        Runnable producer = () -> {
            for(Transaction t: transactions) {
                try {
                    queue.put(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                queue.put(Transaction.buildNullTransaction());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread bankThread = new Thread(producer);
        bankThread.start();

        for (int i = 0; i < TRANS_THREADS; i++) {
            Runnable consumer = () -> {
                try {
                    boolean done = false;
                    while (!done) {
                        Transaction t = queue.take();
                        if (t.isNullTransaction()) {
                            queue.put(t);
                            done = true;
                        } else {
                            transferFunds(t);
                        }
                    }
                }  catch (InterruptedException e) {
                }
            };
            new Thread(consumer).start();
        }

        try {
            bankThread.join();
            accounts.forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void transferFunds(Transaction t) {
        accounts.get(t.getFromAccountId()).withdraw(t.getAmount());
        accounts.get(t.getToAccountId()).deposit(t.getAmount());
    }

}























