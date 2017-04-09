package core_java.exercises;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by ilyarudyak on 4/7/17.
 */
public class SearchWithBlockingQueue {
    private static final int FILE_QUEUE_SIZE = 100;
    private static final int SEARCH_THREADS = 5;
    private static final String DIR = "/Users/ilyarudyak/Desktop/temp/hw4";
    private static final File DUMMY = new File("");

    BlockingQueue<Path> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    private static class FileSearcher implements Runnable {

        @Override
        public void run() {

        }
    }

    void produceFiles() {
        try {
            Files.walk(Paths.get(DIR))
                .filter(Files::isRegularFile)
                .forEach((Path f) -> {
                    try {
//                        System.out.println(f);
                        queue.put(f);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void search(Path path, String searchStr) throws IOException {
        Files.lines(path)
                .forEach(line -> searchInLine(path, line, searchStr));
    }

    void searchInLine(Path path, String line, String searchStr) {
        Arrays.stream(line.split("\\W+"))
                .forEach(word -> {
                    if (word.equals(searchStr)) {
                        System.out.println(path + ":" + line);
                    }
                });
    }

    public static void main(String[] args) throws InterruptedException {
        SearchWithBlockingQueue searcher = new SearchWithBlockingQueue();
        Path path = Paths.get("src/test/resources/small_test.txt");
        ExecutorService executor = Executors.newFixedThreadPool(SEARCH_THREADS);
        executor.execute(() -> searcher.produceFiles());
        IntStream.range(0, SEARCH_THREADS)
                .forEach(n -> executor.submit(() -> {
                    try {

                        searcher.search(path, "public");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
        executor.shutdown();

    }
}
