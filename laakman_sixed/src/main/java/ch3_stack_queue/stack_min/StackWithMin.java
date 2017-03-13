package ch3_stack_queue.stack_min;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StackWithMin<Item extends Comparable<Item>> implements Iterable<Item> {

    private Node<Item> top;
    private int n;
    private Item currentMin;

    public void push(Item item) {
        Node<Item> oldTop = top;
        top = new Node<Item>(new Pair<Item>(item, currentMin), oldTop);
        top.next = oldTop;
        updateMin(item);
        n++;
    }
    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = top.itemPair.item;
        top = top.next;
        n--;
        return item;
    }

    public boolean isEmpty() {
        return top == null;
    }
    public int size() {
        return n;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }
    public Iterator<Item> iterator() {
        return new StackWithMinIterator<Item>(top);
    }

    private void updateMin(Item item) {
        if (isEmpty() || currentMin.compareTo(item) > 0) {
            currentMin = item;
        }
    }

    private static class Node<Item> {
        Pair<Item> itemPair;
        Node<Item> next;

        public Node(Pair<Item> itemPair, Node<Item> next) {
            this.itemPair = itemPair;
            this.next = next;
        }
    }
    private static class Pair<Item> {
        Item item;
        Item minBelow;

        public Pair(Item item, Item minBelow) {
            this.item = item;
            this.minBelow = minBelow;
        }
    }
    private class StackWithMinIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public StackWithMinIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.itemPair.item;
            current = current.next;
            return item;
        }
    }
}
