package liang.coop;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ilyarudyak on 3/28/17.
 */
public class ConsumerProducer {

    private static Buffer buffer = new Buffer();

    public static void main(String[] args) {
        // Create a thread pool with two threads
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ProducerTask());
        executor.execute(new ConsumerTask());
        executor.shutdown();
    }

    // A task for adding an int to the buffer
    private static class ProducerTask implements Runnable {
        public void run() {
            try {
                int i = 1;
                while (true) {
                    System.out.println("Producer writes " + i);
                    buffer.write(i++); // Add a value to the buffer
                    // Put the thread into sleep
                    Thread.sleep((int)(Math.random() * 1000));
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // A task for reading and deleting an int from the buffer
    private static class ConsumerTask implements Runnable {
        public void run() {
            try {
                while (true) {
                    System.out.println("\t\t\tConsumer reads " + buffer.read());
                    // Put the thread into sleep
                    Thread.sleep((int)(Math.random() * 10000));
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // An inner class for buffer
    private static class Buffer {
        private static final int CAPACITY = 3;
        private Deque<Integer> bufferStack = new LinkedList<>();

        // Create a new bufferLock
        private static Lock bufferLock = new ReentrantLock();

        // Create two conditions
        private static Condition notEmptyCondition = bufferLock.newCondition();
        private static Condition notFullCondition = bufferLock.newCondition();

        public void write(int value) {
            bufferLock.lock();
            try {
                while (bufferStack.size() == CAPACITY) {
                    System.out.println("wait for notFullCondition...");
                    notFullCondition.await();
                }

                bufferStack.push(value);
                notEmptyCondition.signal();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                bufferLock.unlock();
            }
        }

        public int read() {
            int value = 0;
            bufferLock.lock();
            try {
                while (bufferStack.isEmpty()) {
                    System.out.println("\t\t\twait for notEmptyCondition...");
                    notEmptyCondition.await();
                }

                value = bufferStack.pop();
                notFullCondition.signal();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                bufferLock.unlock();
                return value;
            }
        }
    }
}
