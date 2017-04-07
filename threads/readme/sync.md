## Synchronizers
* A synchronizer is any object that coordinates the control flow of threads based on 
its state. Blocking queues can act as synchronizers; other types of synchronizers 
include semaphores, barriers, and latches.

### CountDownLatch
* A latch is a synchronizer that can delay the progress of threads until it reaches 
its terminal state. A latch acts as a gate: until the latch reaches the terminal state 
the gate is closed and no thread can pass, and in the terminal state the gate opens, 
allowing all threads to pass. Once the latch reaches the terminal state, it cannot 
change state again, so it remains open forever.
* You can then use a latch to check when all worker threads are done. 
Initialize the latch with the number of threads. Each worker thread counts down that 
latch just before it terminates. Another thread that harvests the work results waits 
on the latch, and proceeds as soon as all workers have terminated. (This is exactly 
how we used latch in cs108 `Bank` class).
* This is an example from [here](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CountDownLatch.html)
(we may use Latch with `Executor`, not necessarily create `Thread` manually):
```java
class Driver { // ...
    void main() throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(N);
        Executor e =  // can we use cash pool here?

        for (int i = 0; i < N; ++i) // create and start threads
            e.execute(new WorkerRunnable(doneSignal, i));

        doneSignal.await();           // wait for all to finish
        // do something when all worker threads done
   }
 }

class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;
    WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }
    public void run() {
        try {
            doWork(i);
            doneSignal.countDown();
        } catch (InterruptedException ex) {} // return;
    }

    void doWork() { /*...*/ }
 }
```

