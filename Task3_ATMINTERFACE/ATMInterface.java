import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String holderName;
    private double balance;

    public BankAccount(String accountNumber, String holderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class ATM {
    private BankAccount bankAccount;
    private String[][] transactions;
    private int transactionCount;

    public ATM(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.transactions = new String[100][2];
        this.transactionCount = 0;
    }

    private void displayHeader() {
        System.out.println("\n========================================");
        System.out.println("           WELCOME TO THE ATM");
        System.out.println("========================================");
    }

    private void displayMenu() {
        System.out.println("\n------------- Main Menu -------------");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Mini Statement");
        System.out.println("5. Exit");
        System.out.println("--------------------------------------");
    }

    private double getValidAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("ERROR: Amount must be greater than zero.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid numeric amount.");
            }
        }
    }

    private void addTransaction(String type, double amount) {
        transactions[transactionCount][0] = type;
        transactions[transactionCount][1] = String.format("%.2f", amount);
        transactionCount++;
    }

    private void checkBalance() {
        System.out.println("\n------------- Account Balance -------------");
        System.out.println("Account Number : " + bankAccount.getAccountNumber());
        System.out.println("Account Holder : " + bankAccount.getHolderName());
        System.out.println("Current Balance: $" + String.format("%.2f", bankAccount.getBalance()));
        System.out.println("-------------------------------------------");
    }

    private void depositMoney(Scanner scanner) {
        System.out.println("\n------------- Deposit Money -------------");
        double amount = getValidAmount(scanner, "Enter amount to deposit: $");

        if (bankAccount.deposit(amount)) {
            addTransaction("DEPOSIT", amount);
            System.out.println("SUCCESS: $" + String.format("%.2f", amount) + " deposited successfully.");
            System.out.println("New Balance: $" + String.format("%.2f", bankAccount.getBalance()));
        } else {
            System.out.println("ERROR: Deposit failed.");
        }
    }

    private void withdrawMoney(Scanner scanner) {
        System.out.println("\n------------- Withdraw Money -------------");
        System.out.println("Available Balance: $" + String.format("%.2f", bankAccount.getBalance()));
        
        double amount = getValidAmount(scanner, "Enter amount to withdraw: $");

        if (amount > bankAccount.getBalance()) {
            System.out.println("ERROR: Insufficient balance. Maximum withdrawal: $" + 
                             String.format("%.2f", bankAccount.getBalance()));
            return;
        }

        if (bankAccount.withdraw(amount)) {
            addTransaction("WITHDRAWAL", amount);
            System.out.println("SUCCESS: $" + String.format("%.2f", amount) + " withdrawn successfully.");
            System.out.println("Remaining Balance: $" + String.format("%.2f", bankAccount.getBalance()));
            System.out.println("\nPLEASE TAKE YOUR CASH.");
        } else {
            System.out.println("ERROR: Withdrawal failed.");
        }
    }

    private void displayMiniStatement() {
        System.out.println("\n------------- Mini Statement -------------");
        if (transactionCount == 0) {
            System.out.println("No transactions yet.");
        } else {
            System.out.printf("%-12s %s%n", "Type", "Amount");
            System.out.println("------------------------------------------");
            for (int i = 0; i < transactionCount; i++) {
                System.out.printf("%-12s $%s%n", transactions[i][0], transactions[i][1]);
            }
            System.out.println("------------------------------------------");
            System.out.printf("%-12s $%.2f%n", "Current Bal:", bankAccount.getBalance());
        }
        System.out.println("-----------------------------------------");
    }

    private int getUserChoice(Scanner scanner) {
        while (true) {
            System.out.print("\nEnter your choice (1-5): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 5) {
                    return choice;
                }
                System.out.println("ERROR: Please enter a number between 1 and 5.");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number.");
            }
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n========================================");
        System.out.println("     CARD AUTHENTICATION IN PROGRESS");
        System.out.println("========================================");
        
        try {
            Thread.sleep(1500);
            System.out.print("Processing");
            for (int i = 0; i < 3; i++) {
                Thread.sleep(500);
                System.out.print(" .");
            }
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n\nAccount authenticated successfully!");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (true) {
            displayHeader();
            displayMenu();

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    depositMoney(scanner);
                    break;
                case 3:
                    withdrawMoney(scanner);
                    break;
                case 4:
                    displayMiniStatement();
                    break;
                case 5:
                    System.out.println("\n========================================");
                    System.out.println("   Thank you for using our ATM!");
                    System.out.println("   Please remove your card.");
                    System.out.println("========================================\n");
                    scanner.close();
                    return;
            }

            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("1234567890", "John Doe", 1000.0);
        ATM atm = new ATM(account);
        atm.start();
    }
}