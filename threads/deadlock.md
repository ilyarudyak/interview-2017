## Deadlock

### Definition
* When `thread A` holds `lock L` and tries to acquire `lock M`, but at the same 
time `thread B` holds `M` and tries to acquire `L`, both threads will wait forever. 
This situation is the simplest case of deadlock.

* In classical `dining philosophers` problem we have 2 strategies: (a) a hungry 
philosopher tries to grab both adjacent chopsticks, but if one is not available, 
puts down the one that is available and waits a minute or so before trying again
(**no** deadlock); (b) each philosopher immediately grabs the chopstick to his left 
and waits for the chopstick to his right to be available before putting down 
the left (**deadlock**);

## Examples
* We have 2 examples from JCIP: (a) in the first example we have methods that 
acquire lock in **different** order; if we call these methods from different 
threads and requests to lock are interleaved we'll get deadlock; (b) in the second
example we have only one method; problems can be cause by **calls** of this method
(we need multiple calls to get this lock and we have an example code to do this):
```
public static void transferMoney(Account fromAccount,
                                 Account toAccount,
                                 DollarAmount amount)
        throws InsufficientFundsException {
    synchronized (fromAccount) {
        synchronized (toAccount) {
            if (fromAccount.getBalance().compareTo(amount) < 0)
                throw new InsufficientFundsException();
            else {
                fromAccount.debit(amount);
                toAccount.credit(amount);
            }
        }
    }
    
Thread A: transferMoney(account #3, account #4, 10);
Thread B: transferMoney(account #4, account #3, 20);
```
* to get rid of this latter deadlock we have to provide ordering (here we use
hash code but we can also use account number in our example):
```
if (fromHash < toHash) { 
    synchronized (fromAcct) {
        synchronized (toAcct) {
            new Helper().transfer();
        } 
    }
} else  { 
    synchronized (toAcct) {
        synchronized (fromAcct) { 
            new Helper().transfer();
        } 
    }
}
```
* so `Thread A` locks `account #3` and now `Thread B` can not lock this account - 
it has to **wait** (like in this story with philosophers);















