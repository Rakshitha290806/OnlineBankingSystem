
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class BankAccount {
    private double balance;

    // Constructor
    public BankAccount(double balance) {
        this.balance = balance;
    }

    // Deposit Method
    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid Deposit Amount");
            return;
        }

        balance += amount;
        System.out.println(Thread.currentThread().getName() +
                " deposited ₹" + amount +
                " | Current Balance: ₹" + balance);
    }

    public synchronized void withdraw(double amount)
            throws InsufficientBalanceException {

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient Balance for withdrawal of ₹" + amount);
        }

        balance -= amount;

        System.out.println(Thread.currentThread().getName() +
                " withdrew ₹" + amount +
                " | Current Balance: ₹" + balance);
    }

    // Balance Check
    public synchronized void checkBalance() {
        System.out.println(Thread.currentThread().getName() +
                " checked balance: ₹" + balance);
    }
}
class UserTransaction extends Thread {

    BankAccount account;

    public UserTransaction(BankAccount account, String name) {
        super(name);
        this.account = account;
    }

    public void run() {

        try {
            account.deposit(5000);

            account.withdraw(3000);

            account.checkBalance();

            // Invalid Transaction
            account.withdraw(20000);

        } catch (InsufficientBalanceException e) {

            System.out.println(getName() +
                    " Exception: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("General Exception: " + e);

        }
    }
}


public class OnlineBankingSystem {

    public static void main(String[] args) {

        // Initial balance
        BankAccount account = new BankAccount(10000);

        // Multiple Users
        UserTransaction user1 =
                new UserTransaction(account, "User-1");

        UserTransaction user2 =
                new UserTransaction(account, "User-2");

        UserTransaction user3 =
                new UserTransaction(account, "User-3");

        // Start Threads
        user1.start();
        user2.start();
        user3.start();
    }
}