package Banking;
/* TODO From Marvin:
*   I used 'LocalDate.parse()' to return a string as a date
* */
import org.joda.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @org.junit.jupiter.api.Test
    void deposit() {
        BankAccount a = new BankAccount(100.0f, LocalDate.parse("25-02-20"), 0.0001f);
        a.deposit(50f, LocalDate.parse("18-03-20"));
        assertEquals(a.balance,150f);
        assertTrue(a.interestEarned>0.299999f && a.interestEarned<0.3000001f);
        assertEquals(a.dayLastOp,61);
    }

    @org.junit.jupiter.api.Test
    void withdraw() {
        BankAccount a = new BankAccount(100.0f, LocalDate.parse("28-02-20"), 0.0001f);
        a.withdraw(50f, LocalDate.parse("18-03-20"));
        assertEquals(a.balance, 50f);
        assertTrue(a.interestEarned>0.299999f && a.interestEarned<0.3000001f);
        assertEquals(a.dayLastOp, 61);

    }

    @org.junit.jupiter.api.Test
    void credit_interest() {
        BankAccount a = new BankAccount(100.0f,LocalDate.parse("01-03-20"),0.0001f);
        a.deposit(50f, LocalDate.parse("10-03-20"));
        a.credit_interest();
        assertEquals(a.balance, 150.3f);
        assertEquals(a.interestEarned, 0.0f);
    }

    @org.junit.jupiter.api.Test
    void BankAccount(){
        BankAccount a = new BankAccount(100.0f, LocalDate.parse("10-02-20"), 0.0001f);
        assertEquals(a.balance, 100.0f);
        assertEquals(a.interestEarned,0.0f);
        assertEquals(a.dayLastOp, 31);
        assertEquals(a.interestRate,0.0001f);
    }

    @org.junit.jupiter.api.Test
    void main() {
    }
}