package ch3_stack_queue;

import java.util.NoSuchElementException;

/**
 * Implement queue using 2 stacks. Ideas:
 * - add elements to stack A - O(1);
 * - when we need an element - move them to stack B - not O(1);
 * - we actually can leave them in stack B (and pop elements
 *   from it until it's not empty);
 * - store new elements in stack A etc.
 */
public class QueueViaStack<T> {

    StackPrinceton<T> stackA;
    StackPrinceton<T> stackB;
    private int n;               // number of elements on queue

    public QueueViaStack() {
        stackA = new StackPrinceton<T>();
        stackB = new StackPrinceton<T>();
        n = 0;
    }

    public boolean isEmpty() {
        return stackA.isEmpty() && stackB.isEmpty();
    }

    public int size() {
        return n;
    }

    public void enqueue(T t) {
        stackA.push(t);
        n++;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        if (stackB.isEmpty()) {
            moveFromAToB();
        }
        n--;
        return stackB.pop();
    }

    private void moveFromAToB() {
        for (T t : stackA) {
            stackB.push(stackA.pop());
        }
    }
}





























