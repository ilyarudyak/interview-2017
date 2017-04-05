package cs108.bank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class Bank {

    private static Integer BANK_ACCOUNTS = 20;
    private static Integer INITIAL_BALANCE = 1000;
    private static Integer CAPACITY = 10;
    private static Integer TRANS_THREADS = 5;
    private static AtomicInteger counter = new AtomicInteger(0);

    private List<Account> accounts;
    private List<Transaction> transactions;
    private BlockingQueue<Transaction> transBQ;
    private CountDownLatch latch = new CountDownLatch(TRANS_THREADS);

    public Bank(String fileName) {
        accounts = Stream.iterate(0, n -> n + 1)
                .limit(BANK_ACCOUNTS)
                .map(n -> new Account(n, INITIAL_BALANCE))
                .collect(Collectors.toList());
        accounts = Collections.synchronizedList(accounts);
        try {
            transactions = Files.lines(Paths.get(fileName))
                    .map(line -> new Transaction(line))
                    .collect(Collectors.toList());
            for (int i = 0; i < TRANS_THREADS; i++) {
                transactions.add(Transaction.buildNullTransaction());
            }
            transactions = Collections.synchronizedList(transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        transBQ = new ArrayBlockingQueue<>(CAPACITY);
    }

    public synchronized void executeTrans(Transaction t) {
        System.out.println(t + " " + counter.incrementAndGet());
        accounts.get(t.getFromAccountId()).withdraw(t.getAmount());
        accounts.get(t.getToAccountId()).deposit(t.getAmount());
    }

    public void processFile() {

        for (int i = 0; i < TRANS_THREADS; i++) {
            Runnable executeTransTask = () -> {
                try {
                    boolean done = false;
                    while (!done) {
                        Transaction t = transBQ.take();
//                        System.out.println(t);
                        if (t.isNullTransaction()) {
                            System.out.println("NULL TRANSACTION!!!!!!!!!!!!!");
                            latch.countDown();
                            done = true;
                        } else {
                            executeTrans(t);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            new Thread(executeTransTask).start();
        }
        new Thread(putTransTask).start();
    }

    Runnable putTransTask = () -> {
        for (Transaction t : transactions) {
            System.out.println(t);
            try {
                transBQ.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        try {
//            latch.await();
//            System.out.println(accounts.size());
//            for(Account a : accounts) {
//                System.out.println(a);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    };

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public BlockingQueue<Transaction> getTransBQ() {
        return transBQ;
    }
}
