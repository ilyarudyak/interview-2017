package ch3_stack_queue.stack_sort;

import ch3_stack_queue.princeton.StackPrinceton;

import java.util.List;

public class StackSorter<T extends Comparable<T>> {

    StackPrinceton<T> leftStack;
    StackPrinceton<T> rightStack;

    public StackSorter(List<T> items) {
        leftStack = new StackPrinceton<T>();
        rightStack = new StackPrinceton<T>();

        for (T item: items) {
            leftStack.push(item);
        }
    }

    public void insertionSort() {
        while (!leftStack.isEmpty()) {
            pushToRightStack();
        }
        pushToLeftStack();
    }

    // helper methods
    private void pushToRightStack() {
        T item = leftStack.pop();
        while (!rightStack.isEmpty()) {
            if (item.compareTo(rightStack.peek()) < 0) {
                leftStack.push(rightStack.pop());
            } else {
                break;
            }
        }
        rightStack.push(item);
    }
    private void pushToLeftStack() {
        while (!rightStack.isEmpty()) {
            leftStack.push(rightStack.pop());
        }
    }
}



















