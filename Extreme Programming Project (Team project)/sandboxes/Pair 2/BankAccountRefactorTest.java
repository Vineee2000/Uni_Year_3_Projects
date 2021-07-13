/* Author: Waleed Bin Asad
Reg: 1805967
*/

public class BankAccountRefactorTest {

        private static void test_creation() {

            BankAccount test1 = standardtestaccount();
            assertBankAccount(test1, 100.0f, 0.0f,31);
            assert test1.interestRate == 0.0001f : "Incorrect interest rate!";
        }

        private static void test_deposit(){
            BankAccount test1 = standardtestaccount();
            test1.deposit(50f, 61);
            assertBankAccount(test1, 150.0f, 0.0f,61);
        }

        private static void test_withdraw(){
            BankAccount test1 = standardtestaccount();
            test1.withdraw(50f, 61);
            assertBankAccount(test1, 50.0f, 0.0f,61);
        }

        private static void test_credit_interest() {
            BankAccount test1 = standardtestaccount();
            test1.deposit(50f, 61);
            test1.credit_interest();
            assertBankAccount(test1, 150.3f, 0.0f,61);
        }

        private static BankAccount standardtestaccount() {
            return new BankAccount(100.0f, 31, 0.0001f);
        }
        private static void assertBankAccount(BankAccount test1, float expectedbalance, float expectedInterestEarned, int expectedDailylastop) {
            assert Math.abs(test1.balance -expectedbalance) <0.001 : "Incorrect balance!";
            assert Math.abs(test1.interestEarned - expectedInterestEarned) <0.001 : "Incorrect interest!";
            assert test1.dayLastOp == expectedDailylastop : "Incorrect day last operation!";
        }
        private static void runAllTests() {
            test_creation();
            test_deposit();
            test_withdraw();
            test_credit_interest();
        }

        public static void main(String[] args) {
            runAllTests();
            System.out.println("Success!");
        }

}
