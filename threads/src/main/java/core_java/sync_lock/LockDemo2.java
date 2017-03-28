package core_java.sync_lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ilyarudyak on 3/28/17.
 */
public class LockDemo2 {

    public static Lock counterLock = new ReentrantLock();
    public static Counter counter = new Counter(0, counterLock);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 1; i <= 100; i++) {
            executor.execute(() -> counter.incrementCounter2(1000));
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("Final value: " + counter.value);
    }


    private static class Counter {

        private int value;
        private Lock counterLock;

        public Counter(int value, Lock counterLock) {
            this.value = value;
            this.counterLock = counterLock;
        }

        private void incrementCounter() {
            counterLock.lock();
            try {
                value++;
            } finally {
                counterLock.unlock();
            }
        }

        private void incrementCounter(int times) {
            for (int k = 1; k <= times; k++) {
                incrementCounter();
            }
        }

        private void incrementCounter2(int times) {
            counterLock.lock();
            try {
                for (int k = 1; k <= times; k++) {
                    value++;
                }
            } finally {
                counterLock.unlock();
            }
        }
    }
}
