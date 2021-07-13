import static org.junit.Assert.*;
import org.junit.*;

public class BankAccountRefactoredTests {
    public static final double DELTA = 0.001;
    private BankAccountRefactored a;

    @Before
    public void setUp() {
        a = standardTestAccount();
    }

    @Test
    public void testCreation(){
        assertBankAccount(100.0f,0.0f,31);
        assert a.interestRate == 0.0001f : "Incorrect Interest Rate!";
    }

    @Test
    public void testDeposit(){
        a.deposit(50f, 61);
        assertBankAccount(150f, 0.3f, 61);
    }

    @Test
    public void testWithdraw(){
        a.withdraw(50f, 61);
        assertBankAccount(50f, 0.3f, 61);
    }

    @Test
    public void testCreditInterest(){
        a.deposit(50f, 61);
        a.credit_interest();
        assertBankAccount(150.3f, 0.0f, 61);
    }

    private static BankAccountRefactored standardTestAccount() {
        return new BankAccountRefactored(100.0f, 31, 0.0001f);
    }

    private void assertBankAccount(float expectedBalance, float expectedInterestEarned, int expectedDayLastOp) {
        assertEquals(expectedBalance, a.balance, DELTA);
        assertEquals(expectedInterestEarned, a.interestEarned, DELTA);
        assertEquals(expectedDayLastOp, a.dayLastOp);
    }
}

