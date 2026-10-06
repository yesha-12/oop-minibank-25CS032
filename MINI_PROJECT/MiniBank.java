import exception.BankException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Account;
import model.Command;
import model.MenuOption;
import model.TransactionType;
import repository.Repository;
import service.BankService;
import util.StatePersister;

public class MiniBank {
    private static final Path DATA_FILE = Path.of("data", "accounts.dat");

    public static void main(String[] args) throws Exception {
        Repository<Account> repository = new Repository<>();
        if (Files.exists(DATA_FILE)) {
            repository.saveAll(StatePersister.load(DATA_FILE));
        }
        BankService service = new BankService(repository);
        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(scanner, service);
        } finally {
            StatePersister.save(repository.findAll().toArray(Account[]::new), DATA_FILE);
        }
    }

    private static void runMenu(Scanner scanner, BankService service)
            throws InterruptedException {
        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                return;
            }
            String choiceText = readLine(scanner, "Choice: ");
            int choice;
            try {
                choice = Integer.parseInt(choiceText.trim());
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid menu number.");
                continue;
            }

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.STATEMENT;
                case 6 -> MenuOption.LIST_ACCOUNTS;
                case 7 -> MenuOption.SEARCH_OWNER;
                case 8 -> MenuOption.REPORT;
                case 9 -> MenuOption.BATCH;
                case 0 -> MenuOption.EXIT;
                default -> null;
            };
            if (option == null) {
                System.out.println("Invalid choice.");
                continue;
            }
            if (option == MenuOption.EXIT) {
                return;
            }
            try {
                switch (option) {
                    case OPEN_ACCOUNT -> openAccount(scanner, service);
                    case DEPOSIT -> service.deposit(readLine(scanner, "Account number: "),
                            readLong(scanner, "Amount: "));
                    case WITHDRAW -> service.withdraw(readLine(scanner, "Account number: "),
                            readLong(scanner, "Amount: "));
                    case TRANSFER -> service.transfer(
                            readLine(scanner, "From account: "),
                            readLine(scanner, "To account: "),
                            readLong(scanner, "Amount: "));
                    case STATEMENT -> System.out.println(service.statement(
                            readLine(scanner, "Account number: ")));
                    case LIST_ACCOUNTS -> service.listAccounts()
                            .forEach(System.out::println);
                    case SEARCH_OWNER -> service.findByOwnerName(
                            readLine(scanner, "Owner name: ")).forEach(System.out::println);
                    case REPORT -> System.out.println(service.generateReport());
                    case BATCH -> runBatch(scanner, service);
                    case EXIT -> {
                        return;
                    }
                }
            } catch (BankException | IOException | IllegalArgumentException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private static void openAccount(Scanner scanner, BankService service)
            throws BankException {
        String type = readLine(scanner, "Type (SAVINGS, CURRENT, FIXED): ");
        String owner = readLine(scanner, "Owner name: ");
        long initialBalance = readLong(scanner, "Initial balance: ");
        Account account = service.openAccount(type, owner, initialBalance);
        System.out.println("Opened " + account);
    }

    private static void runBatch(Scanner scanner, BankService service)
            throws InterruptedException {
        int count = Math.toIntExact(readLong(scanner, "Number of transactions: "));
        if (count < 0 || count > 10000) {
            throw new IllegalArgumentException("Transaction count must be between 0 and 10000");
        }
        ArrayList<Command> commands = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            String type = readLine(scanner, "Transaction " + (index + 1)
                    + " (DEPOSIT/WITHDRAW): ").trim().toUpperCase();
            TransactionType transactionType = TransactionType.valueOf(type);
            String accountNumber = readLine(scanner, "Account number: ");
            long amount = readLong(scanner, "Amount: ");
            commands.add(new Command(transactionType, accountNumber, amount));
        }
        service.processBatch(commands).forEach(System.out::println);
    }

    private static void printMenu() {
        System.out.println("""
                \n===== MiniBank =====
                1. Open account
                2. Deposit
                3. Withdraw
                4. Transfer
                5. View statement
                6. List accounts
                7. Search by owner
                8. Generate report
                9. Process transaction batch
                0. Save and exit""");
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Input ended unexpectedly");
        }
        return scanner.nextLine();
    }

    private static long readLong(Scanner scanner, String prompt) {
        String value = readLine(scanner, prompt);
        return Long.parseLong(value.trim());
    }
}