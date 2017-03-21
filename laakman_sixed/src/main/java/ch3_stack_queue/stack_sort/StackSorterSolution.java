package ch3_stack_queue.stack_sort;


import ch3_stack_queue.princeton.StackPrinceton;

import java.util.Random;

/**
 * Created by ilyarudyak on 3/21/17.
 */
public class StackSorterSolution {

    public static void sort(StackPrinceton<Integer> left) {

        StackPrinceton<Integer> right = new StackPrinceton<Integer>();

        while(!left.isEmpty()) {
			/* Insert each element in s in sorted order into r. */
            int tmp = left.pop();
            while(!right.isEmpty() && right.peek() > tmp) {
                left.push(right.pop());
            }
            right.push(tmp);
        }

		/* Copy the elements back. */
        while (!right.isEmpty()) {
            left.push(right.pop());
        }
    }

    public static void main(String [] args) {

        StackPrinceton<Integer> stack = new StackPrinceton<Integer>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            stack.push(random.nextInt(1000));
        }
        System.out.println(stack);

        sort(stack);
        System.out.println(stack);

    }
}