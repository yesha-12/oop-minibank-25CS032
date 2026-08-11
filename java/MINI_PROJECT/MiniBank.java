import java.util.Scanner;

public class MiniBank {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Anand");

        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        Account[] accounts = new Account[3];

        accounts[0] = new SavingsAccount("Rahul", 10000, 2000);
        accounts[1] = new CurrentAccount("Amit", 0, 3000);
        accounts[2] = new FixedDepositAccount("Neha", 5000);

        accounts[0].deposit(2000);
        accounts[1].deposit(8000);
        accounts[2].withdraw(1000);

        System.out.println("\nInterest Rates:");

        for (Account account : accounts) {
            System.out.println(account.interestRate());

            if (account instanceof FixedDepositAccount) {
                System.out.println("Fixed Deposit account is locked.");
            }
        }

        System.out.println("\nAccount Details:");

        for (Account account : accounts) {
            System.out.println(account);
        }

        System.out.println();

        System.out.println("Account 1 equals Account 2: "
                + accounts[0].equals(accounts[1]));

        Object obj = accounts[0];

        if (obj instanceof Account) {
            System.out.println("Object is an Account");
        }

        Customer.Address address = new Customer.Address(
                "Near Bus Stand",
                "Anand",
                "388001");

        Customer customer = new Customer(
                "Rahul",
                "rahul@gmail.com",
                "9876543210",
                address);

        Customer clonedCustomer = customer.clone();

        System.out.println("\nCustomer ID: " + customer.getCustomerId());
        System.out.println("Cloned Customer Name: " + clonedCustomer.getName());

        while (true) {

            System.out.println("\n===== MiniBank Menu =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            String message = switch (choice) {
                case 1 -> "Open Account - To be implemented later";
                case 2 -> "Deposit - To be implemented later";
                case 3 -> "Withdraw - To be implemented later";
                case 4 -> "Transfer - To be implemented later";
                case 5 -> "Exit";
                default -> "Invalid Choice";
            };

            System.out.println(message);

            if (choice == 5) {
                break;
            }
        }

        System.out.println("Thank You for using MiniBank.");

        sc.close();
    }
}