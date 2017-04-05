package cs108.bank.versions;

import cs108.bank.Bank;
import cs108.bank.Transaction;

import java.util.concurrent.CountDownLatch;

/**
 * Now we're using CountDown latch.
 */
public class BankWithLatch extends Bank {

    private static CountDownLatch endGate = new CountDownLatch(TRANS_THREADS);

    public BankWithLatch(String fileName) {
        super(fileName);
    }

    @Override
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
                            endGate.countDown();
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
            endGate.await();
            accounts.forEach(System.out::println);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
