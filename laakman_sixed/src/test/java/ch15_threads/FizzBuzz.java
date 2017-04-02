package ch15_threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ilyarudyak on 4/2/17.
 */
public class FizzBuzz2 {

    private static NumberPrinter np = new NumberPrinter();

    public static void main(String[] args) {

        Runnable printTask = () -> {
            while (true) {
                np.printNumber();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable checkTask = () -> {
            while (true) {
                np.checkNumber();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(printTask).start();
        new Thread(checkTask).start();

    }

    private static class NumberPrinter {

        private Lock lock = new ReentrantLock();
        private Condition checkNumber = lock.newCondition();
        private Integer number = 0;

        public void printNumber() {
            lock.lock();
            try {
                while (true) {
                    System.out.print(number + " ");
                    checkNumber.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void checkNumber() {
            lock.lock();
            try {
                if (number % 3 == 0 && number % 5 == 0) {
                    System.out.println("FizzBuzz");
                } else if (number % 3 == 0) {
                    System.out.println("Fizz");
                } else if (number % 5 == 0) {
                    System.out.println("Buzz");
                } else {
                    System.out.println();
                }
                number++;
                checkNumber.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
