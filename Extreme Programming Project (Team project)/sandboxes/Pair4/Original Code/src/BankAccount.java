public class BankAccount  {
    float balance, interestEarned, interestRate;
    int dayLastOp;

    public BankAccount(float initialBalance, int dayCreated, float rate) {
        balance = initialBalance;
        dayLastOp = dayCreated;
        interestEarned = 0.0f;
        interestRate = rate;
    }

    public BankAccount() {
        balance = 0.0f;
        dayLastOp = 0;
        interestRate = 0.001f;
        interestEarned = 0.0f;
        // this is a default constructor
        System.out.println("This is a default constructor.");
    }

    public void deposit(float amount, int dayDeposited){
        updateInterest(dayDeposited);
        balance += amount;
    }

    public void withdraw(float amount, int dayWithdrawn){
        updateInterest(dayWithdrawn);
        balance -= amount;
    }

    public void updateInterest(int day) {
        int daysInterest = day - dayLastOp;
        interestEarned += daysInterest * interestRate * balance;
        dayLastOp = day;
    }

    public void credit_interest() {
        balance += interestEarned;
        interestEarned = 0.0f;
    }

    public static void main(String[] args) {
        Test.test_creation();
        Test.test_deposit();
        Test.test_withdraw();
        Test.test_creation();
        Test.test_credit_interest();
        // BankAccount a = new BankAccount ( 100.0f,11,0.001f);
        BankAccount b = new BankAccount();
        // assert a.balance==50.0f : "Incorrect balance!";

        System.out.println("Success!");
    }
    // nice
}
