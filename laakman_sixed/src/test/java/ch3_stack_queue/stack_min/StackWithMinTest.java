package ch3_stack_queue.stack_min;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class StackWithMinTest {

    private StackWithMin<Integer> emptyStack;
    private StackWithMin<Integer> simpleStack;

    @BeforeEach
    void setUp() {
        emptyStack = new StackWithMin<Integer>();

        simpleStack = new StackWithMin<Integer>();
        simpleStack.push(3); simpleStack.push(2);
        simpleStack.push(4); simpleStack.push(1);
        simpleStack.push(5);
    }

    @org.junit.jupiter.api.Test
    void simplePush() {

        emptyStack.push(3);
        assertEquals(Integer.valueOf(3), emptyStack.currentMin);

        emptyStack.push(2);
        assertEquals(Integer.valueOf(2), emptyStack.currentMin);

        emptyStack.push(4);
        assertEquals(Integer.valueOf(2), emptyStack.currentMin);

        emptyStack.push(1);
        assertEquals(Integer.valueOf(1), emptyStack.currentMin);

        emptyStack.push(5);
        assertEquals(Integer.valueOf(1), emptyStack.currentMin);
    }

    @org.junit.jupiter.api.Test
    void simplePop() {

        simpleStack.pop();
        assertEquals(Integer.valueOf(1), simpleStack.currentMin);

        simpleStack.pop();
        assertEquals(Integer.valueOf(2), simpleStack.currentMin);

        simpleStack.pop();
        assertEquals(Integer.valueOf(2), simpleStack.currentMin);

        simpleStack.pop();
        assertEquals(Integer.valueOf(3), simpleStack.currentMin);

        simpleStack.pop();
        assertEquals(null, simpleStack.currentMin);

    }

}