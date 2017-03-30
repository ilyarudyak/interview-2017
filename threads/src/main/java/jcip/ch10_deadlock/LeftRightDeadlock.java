package jcip.ch10_deadlock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ilyarudyak on 3/30/17.
 */
public class LeftRightDeadlock {

    private final Integer left = 1;
    private final Integer right = 2;

    public void leftRight() {
        synchronized (left) {
            System.out.format("%s: leftRight has lock on LEFT: %s%n",
                    Thread.currentThread().getName(), Thread.holdsLock(left));
            Thread.yield();
            System.out.format("%s: leftRight waiting for lock on RIGHT: %s%n",
                    Thread.currentThread().getName(), !Thread.holdsLock(right));
            synchronized (right) {
                System.out.println(addLeftToRight());
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            System.out.format("%s: rightLeft has lock on RIGHT: %s%n",
                    Thread.currentThread().getName(), Thread.holdsLock(right));
            Thread.yield();
            System.out.format("%s: rightLeft waiting for lock on LEFT: %s%n",
                    Thread.currentThread().getName(), !Thread.holdsLock(left));
            synchronized (left) {
                System.out.println(multiplyLeftToRight());
            }
        }
    }

    private Integer addLeftToRight() {
        return left + right;
    }

    private Integer multiplyLeftToRight() {
        return left * right;
    }

    public static void main(String[] args) throws InterruptedException {

        LeftRightDeadlock lrd = new LeftRightDeadlock();
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Runnable> tasks = Arrays.asList(lrd::leftRight, lrd::rightLeft);
        tasks.forEach(executor::execute);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}











