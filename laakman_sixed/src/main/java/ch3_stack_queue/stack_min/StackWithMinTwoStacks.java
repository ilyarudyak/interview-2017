package ch3_stack_queue.stack_min;

import ch3_stack_queue.princeton.StackPrinceton;

import java.util.NoSuchElementException;

/**
 * Created by ilyarudyak on 3/14/17.
 */
public class StackWithMinTwoStacks <Item extends Comparable<Item>> {

    StackPrinceton<Item> mainStack;
    StackPrinceton<Item> minStack;

    public StackWithMinTwoStacks() {
        mainStack = new StackPrinceton<Item>();
        minStack = new StackPrinceton<Item>();
    }

    public void push(Item item) {
        mainStack.push(item);

        if (minStack.isEmpty() || item.compareTo(minStack.peek()) <= 0) {
            minStack.push(item);
        }
    }

    public Item pop() {
        Item item = mainStack.pop();
        if (item.compareTo(minStack.peek()) == 0) {
            minStack.pop();
        }
        return item;
    }

    public Item getMin() {
        return minStack.peek();
    }

    public boolean isEmpty() {
        return mainStack.isEmpty();
    }
    public int size() {
        return mainStack.size();
    }
}
