package Banking;
/* TODO From Marvin:
 *  I used 'LocalDate.parse()' to return a string as a date
 *  Figure out why we have this file
 * */

import org.joda.time.LocalDate;

/**
 * @author Marvin Francis Cudjoe
 * studentID# 1701740
 * Created On: 04/11/2020
 */
public class BankTests {

    public static void test_creation() {
        BankAccount a = new BankAccount(100.0f, LocalDate.parse("10-02-20"), 0.0001f);
        assert a.balance == 100.0f : "Incorrect balance!";
        assert a.interestEarned == 0.0f : "Incorrect interest!";
        assert a.dayLastOp == LocalDate.parse("10-02-20") : "Incorrect day last operation!";
        assert a.interestRate == 0.0001f : "Incorrect interest rate!";
    }

    public static void test_withdraw(){
        BankAccount a = new BankAccount(100.0f,LocalDate.parse("28-02-20"),0.0001f);
        a.withdraw(50f, LocalDate.parse("18-03-20"));
        assert a.balance==50f;
        assert a.interestEarned>0.299999f && a.interestEarned<0.3000001f;
        assert a.dayLastOp == LocalDate.parse("18-03-20");
    }

    public static void test_deposit(){
        test_withdraw();
//        BankAccount a = new BankAccount(100.0f,31,0.0001f);
//        a.deposit(50f, 61);
//        assert a.balance==150f;
//        assert a.interestEarned>0.299999f && a.interestEarned<0.3000001f;
//        assert a.dayLastOp == 61;
    }

    public static void test_credit_interest() {
        BankAccount a = new BankAccount(100.0f,LocalDate.parse("01-03-20"),0.0001f);
        a.deposit(50f, LocalDate.parse("10-03-20"));
        a.credit_interest();
        assert a.balance==150.3f;
        assert a.interestEarned==0.0f;
    }
}
