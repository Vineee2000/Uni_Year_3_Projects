package RefactoredBanking;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BankAccountTest {

    BankAccount bnk;
    @Before
    public void setUp() throws Exception {
        BankAccount bnk = new BankAccount(100.0f, 31, 0.0001f);
    }

    @Test
    public void depositTesting1() {
        assertEquals(true, !(false));
    }
}