package cs108.bank;

import cs108.bank.Account;
import cs108.bank.Transaction;

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
 * This class is based on example from "Core Java":
 * BlockingQueueTest from ch. 14:
 * (1) we don't use CountDown latch here, we simply call join()
 * on banking thread;
 * (2) we also use only 1 dummy value (nullTrans), not TRANS_THREADS;
 * (3) we use flag to stop the Runnable;
 */
public class Bank {

    protected static Integer BANK_ACCOUNTS = 20;
    protected static Integer INITIAL_BALANCE = 1000;
    protected static Integer CAPACITY = 10;
    protected static Integer TRANS_THREADS = 5;

    protected List<Account> accounts;
    protected List<Transaction> transactions;
    protected BlockingQueue<Transaction> queue;

    public Bank() {
        accounts = new CopyOnWriteArrayList<>();
        transactions = new CopyOnWriteArrayList<>();
        queue = new ArrayBlockingQueue<>(CAPACITY);
    }

    public Bank(String fileName) {
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

    public void transferFunds(Transaction t) {
        accounts.get(t.getFromAccountId()).withdraw(t.getAmount());
        accounts.get(t.getToAccountId()).deposit(t.getAmount());
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public BlockingQueue<Transaction> getQueue() {
        return queue;
    }
}























