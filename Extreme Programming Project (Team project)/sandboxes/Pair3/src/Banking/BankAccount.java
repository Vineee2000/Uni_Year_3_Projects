package Banking;
import org.joda.time.Days;
import org.joda.time.LocalDate;

class BankAccount {
    float balance, interestEarned, interestRate;
//    int dayLastOp;
    LocalDate dayLastOp = new LocalDate();


    public BankAccount(float initialBalance, LocalDate dayCreated, float rate) {
        balance = initialBalance;
        dayLastOp = dayCreated;
        interestEarned = 0.0f;
        interestRate = rate;
    }

    public void deposit(float amount, LocalDate dayDeposited){
        Days day = Days.daysBetween(dayDeposited,dayLastOp);
//        int daysInterest = dayDeposited - dayLastOp;
        int daysInterest = day.getDays();
        interestEarned += daysInterest * interestRate * balance;
        balance += amount;
        dayLastOp = dayDeposited;
    }

    public void withdraw(float amount, LocalDate dayWithdrawn){
//        int daysInterest = dayWithdrawn - dayLastOp;
//        interestEarned += daysInterest * interestRate * balance;
//        balance -= amount;
//        dayLastOp = dayWithdrawn;
        deposit(-amount, dayWithdrawn);

    }

    public void credit_interest() {
        balance += interestEarned;
        interestEarned = 0.0f;
    }

    public static void main(String[] args) {
        BankTests.test_creation();
        BankTests.test_deposit();
        BankTests.test_withdraw();
        BankTests.test_credit_interest();
//        new BankAccount(100.0f,11,0.001f);
        BankAccount a = new BankAccount ( 100.0f,LocalDate.parse("20-02-20"),0.001f);
        assert a.balance==50.0f : "Incorrect balance!";
        System.out.println("Success!");
    }
}