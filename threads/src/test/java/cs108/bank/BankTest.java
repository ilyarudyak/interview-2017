package cs108.bank;

import cs108.bank.versions.BankWithLatch;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class BankTest {

    private Bank bankMedium;
    private Bank bankBig;
    private BankWithLatch bankWithLatch;

    @Before
    public void setUp() throws Exception {
        bankMedium = new Bank("src/main/resources/bank/5k.txt");
        bankBig = new Bank("src/main/resources/bank/100k.txt");
        bankWithLatch = new BankWithLatch("src/main/resources/bank/5k.txt");
    }

    @Test
    public void processMedium() throws Exception {
        bankMedium.processTrans();
        for (Account a: bankMedium.getAccounts()) {
            assertEquals(1000, a.getBalance().intValue());
        }
    }

    @Test
    public void processBig() throws Exception {
        bankBig.processTrans();
        for (Account a: bankBig.getAccounts()) {
            assertEquals(1000, a.getBalance().intValue());
        }
    }

    @Test
    public void processWithLatch() throws Exception {
        bankWithLatch.processTrans();
        for (Account a: bankWithLatch.getAccounts()) {
            assertEquals(1000, a.getBalance().intValue());
        }
    }

}













