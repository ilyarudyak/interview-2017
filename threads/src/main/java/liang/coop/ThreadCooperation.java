package liang.coop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ilyarudyak on 3/28/17.
 */
public class ThreadCooperation {

    private static Account account = new Account();

    public static void main(String[] args) {
        // Create a thread pool with two threads
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new DepositTask());
        executor.execute(new WithdrawTask());
        executor.shutdown();

        System.out.println("Thread 1\t\tThread 2\t\tBalance");
    }

    // A task for adding an amount to the account
    public static class DepositTask implements Runnable {
        public void run() {
            try { // Purposely delay it to let the withdraw method proceed
                while (true) {
                    account.deposit((int)(Math.random() * 10) + 1);
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // A task for subtracting an amount from the account
    public static class WithdrawTask implements Runnable {
        public void run() {
            while (true) {
                account.withdraw((int)(Math.random() * 10) + 1);
            }
        }
    }

    // An inner class for account
    private static class Account {

        private static Lock accountLock = new ReentrantLock();
        private static Condition accountLockNewDepositCondition = accountLock.newCondition();

        private int balance = 0;

        public int getBalance() {
            return balance;
        }

        public void withdraw(int amount) {
            accountLock.lock(); // Acquire the accountLock
            try {
                while (balance < amount) {
                    System.out.println("\t\t\tWait for a deposit");
                    accountLockNewDepositCondition.await();
                }
                balance -= amount;
                System.out.println("\t\t\tWithdraw " + amount +
                        "\t\t" + getBalance());
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            finally {
                accountLock.unlock(); // Release the accountLock
            }
        }

        public void deposit(int amount) {
            accountLock.lock(); // Acquire the accountLock
            try {
                balance += amount;
                System.out.println("Deposit " + amount +
                        "\t\t\t\t\t" + getBalance());

                // Signal thread waiting on the condition
                accountLockNewDepositCondition.signalAll();
            }
            finally {
                accountLock.unlock(); // Release the accountLock
            }
        }
    }
}
