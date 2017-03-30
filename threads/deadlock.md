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
example we have only one method; problems can be cause by **calls** of this method:
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
    
Thread A: transferMoney(myAccount, yourAccount, 10);
Thread B: transferMoney(yourAccount, myAccount, 20);
```