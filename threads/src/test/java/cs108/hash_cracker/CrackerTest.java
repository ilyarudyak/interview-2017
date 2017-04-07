package cs108.hash_cracker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ilyarudyak on 4/7/17.
 */
public class CrackerTest {
    private Cracker cracker;

    @Before
    public void setUp() throws Exception {
        cracker = new Cracker();
    }

    @Test
    public void charsSimple() throws Exception {
        assertEquals("CopyOnWriteArrayList", Cracker.CHARS.getClass().getSimpleName().toString());
        assertEquals("Character", Cracker.CHARS.get(0).getClass().getSimpleName().toString());
        assertEquals(40, Cracker.CHARS.size());
        assertEquals(new Character('a'), Cracker.CHARS.get(0));
    }

    @Test
    public void getHashSimple() throws Exception {
        assertEquals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", cracker.getHash("a"));
        assertEquals("adeb6f2a18fe33af368d91b09587b68e3abcb9a7", cracker.getHash("fm"));
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", cracker.getHash("a!"));
        assertEquals("66b27417d37e024c46526c2f6d358a754fc552f3", cracker.getHash("xyz"));
    }

    @Test
    public void crackPassSimple() throws Exception {
        cracker.crackPass("adeb6f2a18fe33af368d91b09587b68e3abcb9a7");
    }
}




















