package cs108.bank;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class BankTest {

    private Bank bankSmall;

    @Before
    public void setUp() throws Exception {
        bankSmall = new Bank("src/main/resources/bank/5k.txt");
    }

    @Test
    public void constructorSmall() {
        assertEquals(20, bankSmall.getAccounts().size());
        assertEquals(15, bankSmall.getTransactions().size());
        for (int i = 10; i < 14; i++) {
            assertTrue(bankSmall.getTransactions().get(i).isNullTransaction());
        }
    }

    @Test
    public void transfer() throws Exception {
        bankSmall.executeTrans(new Transaction("0 1 1"));
        bankSmall.getAccounts().forEach(System.out::println);
    }

    @Test
    public void processFile() throws Exception {
        bankSmall.processFile();
    }

}