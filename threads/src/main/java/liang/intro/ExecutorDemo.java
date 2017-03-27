package liang.intro;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ilyarudyak on 3/27/17.
 */
public class ExecutorDemo {

    public static void main(String[] args) {
        // Create a fixed thread pool with maximum three threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit runnable tasks to the executor
        List<Runnable> tasks = Arrays.asList(
                new PrintChar('a', 100),
                new PrintChar('b', 100),
                new PrintNum(100)
        );
        tasks.forEach(executor::execute);

        // Shut down the executor
        executor.shutdown();
    }
}
