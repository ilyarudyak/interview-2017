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
public class Wizard2 {
    private final String name;
    private final Set<Wizard2> friends;
    // Rep invariant:
    //    name, friends != null
    //    friend links are bidirectional:
    //        for all f in friends, f.friends contains this
    // Concurrency argument:
    //    threadsafe by monitor pattern: all accesses to rep
    //    are guarded by this object's lock

    public Wizard2(String name) {
        this.name = name;
        this.friends = new HashSet<>();
    }

    public boolean isFriendsWith(Wizard2 that) {
        synchronized (this) {
            synchronized (that) {
                return this.friends.contains(that);
            }
        }
    }

    public void friend(Wizard2 that) {
        synchronized (this) {
            System.out.format("%s: has lock on THIS: %s%n",
                    Thread.currentThread().getName(), Thread.holdsLock(this));
            Thread.yield();
            System.out.format("%s: waiting for lock on THAT: %s%n",
                    Thread.currentThread().getName(), !Thread.holdsLock(that));
            synchronized (that) {
                if (friends.add(that)) {
                    that.friend(this);
                    System.out.println(friends);
                }
            }
        }

    }

    public synchronized void defriend(Wizard2 that) {
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

    @Override
    public String toString() {
        return "Wizard: " + name;
    }

    public static void main(String[] args) throws InterruptedException {

        final Wizard2 harry = new Wizard2("Harry Potter");
        final Wizard2 snape = new Wizard2("Severus Snape");


        List<Runnable> tasks = Arrays.asList(
                () -> { harry.friend(snape);
                        /*harry.defriend(snape);*/},
                () -> { harry.defriend(snape);
                        /*snape.friend(harry);*/
                        /*harry.friend(snape);*/}
        );

        new Thread(tasks.get(0)).start();
        new Thread(tasks.get(1)).start();
    }
}
