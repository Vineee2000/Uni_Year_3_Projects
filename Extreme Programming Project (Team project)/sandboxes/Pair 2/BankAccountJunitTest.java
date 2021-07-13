import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BankAccountJunitTest {

    private BankAccount test1 ;
    private static final double DELTA = 0.001;

    @Before
    public void setUp()  {
        test1 = standardtestaccount();
        assert test1.interestRate == 0.0001f: "Incorrect, Interest Rate" ;
    }

    @Test
    public void deposit() {
        test1.deposit(50f, 61);
        assertBankAccount(test1, 150.0f, 0.3f,61);
    }

    @Test
    public void withdraw() {
        test1.withdraw(50f, 61);
        assertBankAccount(test1, 50.0f, 0.3f,61);
    }

    @Test
    public void credit_interest() {
        test1.deposit(50f, 61);
        test1.credit_interest();
        assertBankAccount(test1, 150.3f, 0.0f,61);
    }

    private static BankAccount standardtestaccount() {
        return new BankAccount(100.0f, 31, 0.0001f);
    }
    private static void assertBankAccount(BankAccount test1, float expectedbalance, float expectedInterestEarned, int expectedDailylastop) {

        assertEquals( expectedbalance,test1.balance, DELTA);
        assertEquals( expectedInterestEarned,test1.interestEarned, DELTA);
        assertEquals(expectedDailylastop,test1.dayLastOp);
    }
}