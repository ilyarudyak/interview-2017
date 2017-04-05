package cs108.bank;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class Transaction {
    private final Integer fromAccountId;
    private final Integer toAccountId;
    private final Integer amount;

    public Transaction(Integer fromAccountId, Integer toAccountId, Integer amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Transaction(String[] trans) {
        this(   Integer.parseInt(trans[0]),
                Integer.parseInt(trans[1]),
                Integer.parseInt(trans[2])
        );
    }

    public static Transaction buildNullTransaction() {
        return new Transaction(-1, 0, 0);
    }

    public synchronized boolean isNullTransaction() {
        return fromAccountId.equals(new Integer(-1)) &&
                toAccountId.equals((new Integer(0))) &&
                amount.equals(new Integer(0));
    }

    public Transaction(String transStr) {
        this(transStr.split("\\s"));
    }

    public Integer getFromAccountId() {
        return new Integer(fromAccountId);
    }

    public Integer getToAccountId() {
        return new Integer(toAccountId);
    }

    public Integer getAmount() {
        return new Integer(amount.intValue());
    }

    @Override
    public String toString() {
        return String.format("Transaction from:%d to:%d amount:$%d",
                fromAccountId, toAccountId, amount.intValue());
    }
}












