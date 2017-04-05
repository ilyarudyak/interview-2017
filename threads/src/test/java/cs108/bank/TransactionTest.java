package cs108.bank;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class TransactionTest {

    private Transaction transaction;

    @Before
    public void setUp() throws Exception {
        transaction = new Transaction("17 6 104");
    }

    @Test
    public void simpleTest() throws Exception {
        System.out.println(transaction);
        assertEquals(transaction.getFromAccountId(), new Integer(17));
        assertEquals(transaction.getToAccountId(), new Integer(6));
        assertEquals(transaction.getAmount(), new Integer(104));
    }

}