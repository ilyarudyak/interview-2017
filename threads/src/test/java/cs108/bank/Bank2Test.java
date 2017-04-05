package cs108.bank;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ilyarudyak on 4/5/17.
 */
public class Bank2Test {

    private Bank2 bankMedium;
    private Bank2 bankBig;

    @Before
    public void setUp() throws Exception {
        bankMedium = new Bank2("src/main/resources/bank/5k.txt");
        bankBig = new Bank2("src/main/resources/bank/100k.txt");
    }

    @Test
    public void processMedium() throws Exception {
        bankMedium.processTrans();
    }

    @Test
    public void processBig() throws Exception {
        bankBig.processTrans();
    }

}