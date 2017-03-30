package jcip.ch10_deadlock;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 3/30/17.
 */
public class InduceLockOrder {
    private static final Object tieLock = new Object();

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1_000_000;

    public static void transferMoneyThreadSafe(final DynamicOrderDeadlock.Account fromAccount, final DynamicOrderDeadlock.Account toAccount, DynamicOrderDeadlock.DollarAmount amount) {

        class Helper {
            public void transfer() {
                try {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                } catch (DynamicOrderDeadlock.InsufficientFundsException e) {
                }
            }
        }

        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccount);

        synchronized (fromAccount) {
            synchronized (toAccount) {
                try {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                } catch (DynamicOrderDeadlock.InsufficientFundsException e) {
                }
            }
        }

        if (fromHash < toHash) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    public static void safeExecution() throws InterruptedException {
        final Random rnd = new Random(Instant.now().getEpochSecond());
        List<DynamicOrderDeadlock.Account> accounts = Stream.generate(() -> new DynamicOrderDeadlock.Account(100))
                .limit(NUM_ACCOUNTS)
                .collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Runnable> tasks = Stream.generate(() -> buildTransferTaskThreadSafe(accounts, rnd))
                .limit(NUM_ITERATIONS)
                .collect(Collectors.toList());

        tasks.forEach(executor::execute);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println(accounts);

    }

    private static Runnable buildTransferTaskThreadSafe(List<DynamicOrderDeadlock.Account> accounts, Random rnd) {
        return new Runnable() {
            @Override
            public void run() {
                DynamicOrderDeadlock.Account A = accounts.get(rnd.nextInt(NUM_ACCOUNTS));
                DynamicOrderDeadlock.Account B = accounts.get(rnd.nextInt(NUM_ACCOUNTS));
                DynamicOrderDeadlock.DollarAmount amount = new DynamicOrderDeadlock.DollarAmount(1);
                transferMoneyThreadSafe(A, B, amount);
            }
        };
    }

}
