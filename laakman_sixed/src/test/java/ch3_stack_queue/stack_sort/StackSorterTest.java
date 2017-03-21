package ch3_stack_queue.stack_sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ilyarudyak on 3/21/17.
 */
class StackSorterTest {

    private StackSorter<Integer> stackSorter;

    @BeforeEach
    void setUp() {
        stackSorter = new StackSorter<Integer>(Arrays.asList(2,5,3,1,4));
    }

    @Test
    void simpleStack() {
        stackSorter.insertionSort();
        System.out.println(stackSorter.leftStack);
    }
}