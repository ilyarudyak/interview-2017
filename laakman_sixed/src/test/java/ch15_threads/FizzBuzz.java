package ch15_threads;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  In the classic problem FizzBuzz, you are told to print the numbers from 1 to n.
 *  However, when the number is divisible by 3, print "Fizz': When it is divisible
 *  by 5, print "Buzz': When it is divisible by 3 and 5, print "FizzBuzz':
 *  In this problem, you are asked to do this in a multithreaded way.
 *  Implement a multithreaded version of FizzBuzz with four threads.
 *  One thread checks for divisibility of 3 and prints "Fizz': Another thread is
 *  responsible for divisibility of 5 and prints "Buzz': A third thread is responsible
 *  for divisibility of 3 and 5 and prints "FizzBuzz". A fourth thread does the numbers.
 */
public class FizzBuzz {

    private List<Integer> numbers;
    private AtomicInteger index;
    private AtomicInteger N;
    private Lock numbersLock;
    private Condition checkNumberCondition;

    public FizzBuzz(int n) {
        N = new AtomicInteger(n);
        numbers = Collections.synchronizedList(
                Stream.iterate(1, x -> x + 1)
                        .limit(N.intValue())
                        .collect(Collectors.toList())
        );
        index = new AtomicInteger(0);
        numbersLock = new ReentrantLock();
        checkNumberCondition = numbersLock.newCondition();
    }

    public void printNumbers() {
        numbersLock.lock();

        try {
            while (index.intValue() < N.intValue()) {
                System.out.print(numbers.get(index.intValue()) + " ");
                checkNumberCondition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numbersLock.unlock();
    }

    public void checkDiv3PrintFizz() {
        numbersLock.lock();
        if (numbers.get(index.intValue()) % 3 == 0) {
            System.out.println("Fizz");
        } else {
            System.out.println();
        }
        index.incrementAndGet();
        checkNumberCondition.signal();
        numbersLock.unlock();
    }

    public static void main(String[] args) {
        FizzBuzz fb = new FizzBuzz(10);
        new Thread(() -> {
                while (fb.index.intValue() < fb.N.intValue()) {
                    fb.printNumbers();
                }
        }).start();
        new Thread(() -> {
            while (fb.index.intValue() < fb.N.intValue()) {
                fb.checkDiv3PrintFizz();
            }
        }).start();
    }
}


















