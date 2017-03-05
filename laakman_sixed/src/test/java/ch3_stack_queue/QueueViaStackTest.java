package ch3_stack_queue;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ilyarudyak on 3/6/17.
 */
class QueueViaStackTest {

    private QueueViaStack<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new QueueViaStack<Integer>();
    }

    @org.junit.jupiter.api.Test
    void simpleQueue() {

        queue.enqueue(1);
        queue.enqueue(3);
        queue.enqueue(5);
        assertEquals(3, queue.size());

        assertEquals(Integer.valueOf(1), queue.dequeue());
        assertEquals(Integer.valueOf(3), queue.dequeue());
        assertEquals(1, queue.size());

        queue.enqueue(7);
        queue.enqueue(9);
        assertEquals(Integer.valueOf(5), queue.dequeue());
        assertEquals(Integer.valueOf(7), queue.dequeue());
        assertEquals(1, queue.size());

    }


}