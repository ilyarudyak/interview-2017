## Blocking queue
* Producer tasks insert items into the queue, and consumer tasks retrieve them. 
When you try to add an element and the queue is currently full, or you try 
to remove an element when the queue is empty, the operation blocks. In this way, 
the queue balances the workload.
* A common challenge with such a design is stopping the consumers. A consumer cannot 
simply quit when the queue is empty. After all, the producer might not yet have started, 
or it may have fallen behind. If there is a single producer, it can add a “last item” 
indicator to the queue, similar to a dummy suitcase with a label “last bag” in a 
baggage claim belt.
```java
// from Core java
public class BlockingQueueDemo {
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);
    
    public static void main(String[] args) {
        Runnable enumerator = () -> {
            // enumerate files in dir recursively
            queue.put(DUMMY);    
        };
        new Thread(enumerator).start();
        for (int i = 1; i <= SEARCH_THREADS; i++) {
            Runnable searcher = () -> {
                boolean done = false;
                while (!done) {
                    File file = queue.take();
                    if (file == DUMMY) {
                        queue.put(file);
                        done = true;
                    } else {
                        // search in the file
                    }
                }
            };
        new Thread(searcher).start();
        }
    }
}
```
  