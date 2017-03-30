package jcip.ch10_deadlock;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 3/30/17.
 */
public class DynamicOrderDeadlock {

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1_000_000;

    // Warning: deadlock-prone!
    public static void transferMoneyNotThreadSafe(Account fromAccount, Account toAccount, DollarAmount amount) {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                try {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                } catch (InsufficientFundsException e) {
                }
            }
        }
    }

    static class DollarAmount implements Comparable<DollarAmount> {
        private Integer amount;

        public DollarAmount(Integer amount) {
            this.amount = amount;
        }

        public DollarAmount add(DollarAmount dollarAmount) {
            return new DollarAmount(amount + dollarAmount.amount);
        }

        public DollarAmount subtract(DollarAmount dollarAmount) {
            return new DollarAmount(amount - dollarAmount.amount);
        }

        public int compareTo(DollarAmount dollarAmount) {
            return amount - dollarAmount.amount;
        }

        @Override
        public String toString() {
            return "$" + amount;
        }
    }

    static class Account {
        private DollarAmount balance;
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();

        public Account(Integer balance) {
            this.balance = new DollarAmount(balance);
            acctNo = sequence.incrementAndGet();
        }

        void debit(DollarAmount d) throws InsufficientFundsException {
            if (balance.compareTo(d) > 0) {
                balance = balance.subtract(d);
            } else {
                throw new InsufficientFundsException();
            }
        }

        void credit(DollarAmount d)  {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        int getAcctNo() {
            return acctNo;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "balance=" + balance +
                    ", acctNo=" + acctNo +
                    '}';
        }
    }

    static class InsufficientFundsException extends Exception {
    }

    public static void deadLockWithYield() throws InterruptedException {
        Account A = new Account(100);
        Account B = new Account(100);

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Runnable> tasks = Arrays.asList(
                () -> transferMoneyNotThreadSafe(A, B, new DollarAmount(50)),
                () -> transferMoneyNotThreadSafe(B, A, new DollarAmount(25))
        );
        tasks.forEach(executor::execute);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void realDeadlock() throws InterruptedException {
        final Random rnd = new Random(Instant.now().getEpochSecond());
        List<Account> accounts = Stream.generate(() -> new Account(100))
                .limit(NUM_ACCOUNTS)
                .collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Runnable> tasks = Stream.generate(() -> buildTransferTask(accounts, rnd))
                .limit(NUM_ITERATIONS)
                .collect(Collectors.toList());

        tasks.forEach(executor::execute);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(accounts);

    }

    private static Runnable buildTransferTask(List<Account> accounts, Random rnd) {
        return new Runnable() {
            @Override
            public void run() {
                Account A = accounts.get(rnd.nextInt(NUM_ACCOUNTS));
                Account B = accounts.get(rnd.nextInt(NUM_ACCOUNTS));
                DollarAmount amount = new DollarAmount(1);
                transferMoneyNotThreadSafe(A, B, amount);
            }
        };
    }

    public static void main(String[] args) throws InsufficientFundsException, InterruptedException {
        realDeadlock();
    }
}
















