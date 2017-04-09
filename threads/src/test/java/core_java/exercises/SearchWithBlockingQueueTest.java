package core_java.exercises;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by ilyarudyak on 4/7/17.
 */
public class SearchWithBlockingQueueTest {

    private SearchWithBlockingQueue searcher;

    @Before
    public void setUp() throws Exception {
        searcher = new SearchWithBlockingQueue();
    }

    @Test
    public void produceFiles() throws Exception {
        Thread t = new Thread(() -> searcher.produceFiles());
        t.start();
        t.join();
        System.out.println(searcher.queue);
    }

    @Test
    public void searchSimple() throws Exception {
        String fileName = "src/test/resources/small_test.txt";
        searcher.search(Paths.get(fileName), "public");
    }
}