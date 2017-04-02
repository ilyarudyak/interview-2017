package mit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by ilyarudyak on 4/2/17.
 */
public class Wizard {
    private final String name;
    private final Set<Wizard> friends;
    // Rep invariant:
    //    name, friends != null
    //    friend links are bidirectional:
    //        for all f in friends, f.friends contains this
    // Concurrency argument:
    //    threadsafe by monitor pattern: all accesses to rep
    //    are guarded by this object's lock

    public Wizard(String name) {
        this.name = name;
        this.friends = new HashSet<>();
    }

    public boolean isFriendsWith(Wizard that) {
        synchronized (this) {
            synchronized (that) {
                return this.friends.contains(that);
            }
        }
    }

    public void friend(Wizard that) {
        synchronized (this) {
            System.out.format("%s: has lock on THIS: %s%n",
                    Thread.currentThread().getName(), Thread.holdsLock(this));
            Thread.yield();
            System.out.format("%s: waiting for lock on THAT: %s%n",
                    Thread.currentThread().getName(), !Thread.holdsLock(that));
            synchronized (that) {
                if (friends.add(that)) {
                    that.friend(this);
                }
            }
        }

    }

    public synchronized void defriend(Wizard that) {
        synchronized (this) {
            System.out.format("%s: has lock on THIS: %s%n",
                    Thread.currentThread().getName(), Thread.holdsLock(this));
            Thread.yield();
            System.out.format("%s: waiting for lock on THAT: %s%n",
                    Thread.currentThread().getName(), !Thread.holdsLock(that));
            synchronized (that) {
                if (friends.remove(that)) {
                    that.defriend(this);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        final Wizard harry = new Wizard("Harry Potter");
        final Wizard snape = new Wizard("Severus Snape");

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Runnable> tasks = Arrays.asList(
                () -> harry.friend(snape),
                () -> snape.friend(harry)
        );
        tasks.forEach(executor::execute);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
