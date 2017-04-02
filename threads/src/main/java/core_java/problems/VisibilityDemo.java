package core_java.problems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ilyarudyak on 4/2/17.
 */
public class VisibilityDemo {

    private static volatile boolean done = false; // add volatile

    public static void main(String[] args) throws InterruptedException {
        Runnable hellos = () -> {
            for (int i = 1; i <= 1000; i++)
                System.out.println("Hello " + i);
            done = true;
        };
        Runnable goodbye = () -> {
            int i = 1;
            while (!done) i++;
            System.out.println("Goodbye " + i);
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(hellos);
        executor.execute(goodbye);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
