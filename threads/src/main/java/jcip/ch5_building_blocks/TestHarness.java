package jcip.ch5_building_blocks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class TestHarness {

    private static final Integer THREADS = 10;

    private static final CountDownLatch startGate = new CountDownLatch(1);
    private static final CountDownLatch endGate = new CountDownLatch(THREADS);

    public static long timeTasks(final Runnable task)
            throws InterruptedException {

        for (int i = 0; i < THREADS; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException ignored) {
                }
            });
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(timeTasks(() -> {
            AtomicInteger counter = new AtomicInteger(0);
            for (int i = 0; i < 10_000_000; i++) {
                counter.incrementAndGet();
            }
        }));
    }
}
