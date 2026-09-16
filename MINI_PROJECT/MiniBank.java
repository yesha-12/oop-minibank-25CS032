import java.util.Scanner;

import model.Account;
import model.BankInfo;
import model.Command;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.MenuOption;
import model.SavingsAccount;
import service.WithdrawRule;
import util.AnnotationValidator;
import util.CommandParser;
import util.StatementFormatter;

public class MiniBank {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Anand");

        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        Account invalidAccount = new SavingsAccount("Validation Test", -100, 0);
        for (String error : AnnotationValidator.validate(invalidAccount)) {
            System.out.println(error);
        }

        Account[] accounts = new Account[3];

        accounts[0] = new SavingsAccount("Rahul", 10000, 2000);
        accounts[1] = new CurrentAccount("Amit", 0, 3000);
        accounts[2] = new FixedDepositAccount("Neha", 5000);

        accounts[0].deposit(2000);
        accounts[1].deposit(8000);
        accounts[2].withdraw(1000);

        System.out.println("\nInterest Rates:");

        for (Account account : accounts) {
                System.out.println(account.interestRate() + "% (yearly: "
                    + account.yearlyInterest() + ")");

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

        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.canWithdraw(amount);
            }
        };
        WithdrawRule lambdaRule = (account, amount) -> account.canWithdraw(amount);
        System.out.println("Withdrawal rules: "
                + anonymousRule.allow(accounts[0], 500) + ", "
                + lambdaRule.allow(accounts[1], 500));

        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("Parsed command: " + command.type() + " "
                + command.accountNumber() + " " + command.amount());
        System.out.println(StatementFormatter.buildStatement(accounts[0]));

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

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            String message = option == null ? "Invalid Choice" : switch (option) {
                case OPEN_ACCOUNT -> "Open Account - To be implemented later";
                case DEPOSIT -> "Deposit - To be implemented later";
                case WITHDRAW -> "Withdraw - To be implemented later";
                case TRANSFER -> "Transfer - To be implemented later";
                case EXIT -> "Exit";
            };

            System.out.println(message);

            if (option == MenuOption.EXIT) {
                break;
            }
        }

        System.out.println("Thank You for using MiniBank.");

        sc.close();
    }
}