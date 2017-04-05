package cs108.bank;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class Account {

    private final Integer id;
    private AtomicInteger balance;
    private AtomicInteger transCount;

    public Account(Integer id, Integer balance) {
        this.id = id;
        this.balance = new AtomicInteger(balance);
        this.transCount = new AtomicInteger(0);
    }

    public void deposit(Integer amount) {
        balance.addAndGet(amount);
        transCount.incrementAndGet();
    }

    public void withdraw(Integer amount) {
        balance.addAndGet(-amount);
        transCount.incrementAndGet();
    }

    @Override
    public String toString() {
        return String.format("Account id:%d balance:$%d transCount:%d",
                id, balance.intValue(), transCount.intValue());
    }

    public static void main(String[] args) {
        List<Account> accounts = Stream.iterate(0, n -> n + 1)
                .limit(10)
                .map(n -> new Account(new Integer((int) n), new Integer(1000)))
                .collect(Collectors.toList());
        accounts.forEach(System.out::println);
    }
}




























