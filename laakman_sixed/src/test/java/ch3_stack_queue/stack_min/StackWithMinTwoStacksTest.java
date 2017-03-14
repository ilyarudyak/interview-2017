package ch3_stack_queue.stack_min;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ilyarudyak on 3/14/17.
 */
class StackWithMinTwoStacksTest {

    private StackWithMinTwoStacks<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new StackWithMinTwoStacks<Integer>();
    }

    @Test
    void push() {
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(1);
        stack.push(5);

        assertEquals(Integer.valueOf(1), stack.minStack.pop());
        assertEquals(Integer.valueOf(2), stack.minStack.pop());
        assertTrue(stack.minStack.isEmpty());
    }

    @Test
    void pop() {
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(1);
        stack.push(5);

        stack.pop();
        assertEquals(Integer.valueOf(1), stack.getMin());
        stack.pop();
        assertEquals(Integer.valueOf(2), stack.getMin());
        stack.pop();
        assertEquals(Integer.valueOf(2), stack.getMin());
    }

}