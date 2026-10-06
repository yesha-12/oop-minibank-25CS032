package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Account;
import model.Command;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.TransactionType;
import repository.Repository;
import util.ReportGenerator;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

public class BankService {
    private final Repository<Account> accounts;
    private final Path logFile;
    private final Path logsFolder;
    private final Path reportFile;

    public BankService(Repository<Account> accounts) {
        this(accounts, Path.of("data", "logs", "transactions.log"),
                Path.of("data", "logs"), Path.of("data", "daily-report.txt"));
    }

    public BankService(Repository<Account> accounts, Path logFile,
            Path logsFolder, Path reportFile) {
        this.accounts = accounts;
        this.logFile = logFile;
        this.logsFolder = logsFolder;
        this.reportFile = reportFile;
    }

    public Account openAccount(String type, String ownerName, long initialBalance)
            throws InvalidAmountException {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Account type cannot be blank");
        }
        if (!Validator.isValidOwnerName(ownerName)) {
            throw new IllegalArgumentException("Owner name cannot be blank");
        }
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative");
        }
        Account account = switch (type.trim().toUpperCase()) {
            case "SAVINGS" -> new SavingsAccount(ownerName.trim(), initialBalance, 0);
            case "CURRENT" -> new CurrentAccount(ownerName.trim(), initialBalance, 0);
            case "FIXED", "FIXED_DEPOSIT" ->
                    new FixedDepositAccount(ownerName.trim(), initialBalance);
            default -> throw new IllegalArgumentException("Unknown account type: " + type);
        };
        accounts.save(account);
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException {
        requirePositive(amount);
        Account account = requireAccount(accountNumber);
        account.deposit(amount);
        append("DEPOSIT " + accountNumber + " " + amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException {
        requirePositive(amount);
        Account account = requireAccount(accountNumber);
        if (!account.withdraw(amount)) {
            throw new InsufficientFundsException(
                    "Withdrawal is not allowed for account " + accountNumber, amount);
        }
        append("WITHDRAW " + accountNumber + " " + amount);
    }

    public void transfer(String sourceNumber, String destinationNumber, long amount)
            throws BankException {
        requirePositive(amount);
        Account source = requireAccount(sourceNumber);
        Account destination = requireAccount(destinationNumber);
        if (source.equals(destination)
                || !TransactionProcessor.transfer(source, destination, amount)) {
            throw new InsufficientFundsException(
                    "Transfer is not allowed from account " + sourceNumber, amount);
        }
        append("WITHDRAW " + sourceNumber + " " + amount);
        append("DEPOSIT " + destinationNumber + " " + amount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(requireAccount(accountNumber));
    }

    public List<Account> listAccounts() {
        return accounts.findAll();
    }

    public List<Account> findByOwnerName(String ownerName) {
        String query = ownerName.trim().toLowerCase();
        ArrayList<Account> matches = new ArrayList<>();
        for (Account account : accounts.findAll()) {
            if (account.getOwnerName().toLowerCase().contains(query)) {
                matches.add(account);
            }
        }
        return matches;
    }

    public String generateReport() throws IOException {
        return ReportGenerator.generate(logsFolder, reportFile);
    }

    public List<String> processBatch(List<Command> commands)
            throws InterruptedException {
        if (commands == null) {
            throw new IllegalArgumentException("Batch cannot be null");
        }
        if (commands.contains(null)) {
            throw new IllegalArgumentException("Batch cannot contain null commands");
        }
        TransactionProcessor processor =
                new TransactionProcessor(accounts.asConcurrentMap());
        String[] results = new String[commands.size()];
        for (int index = 0; index < commands.size(); index++) {
            int resultIndex = index;
            Command command = commands.get(index);
            processor.submit(() -> {
                try {
                    if (command.type() == TransactionType.DEPOSIT) {
                        deposit(command.accountNumber(), command.amount());
                    } else if (command.type() == TransactionType.WITHDRAW) {
                        withdraw(command.accountNumber(), command.amount());
                    } else {
                        throw new IllegalArgumentException(
                                "Batch transfers require an explicit destination");
                    }
                    results[resultIndex] = "OK " + command.accountNumber();
                } catch (BankException | IllegalArgumentException exception) {
                    results[resultIndex] = "ERROR " + exception.getMessage();
                }
            });
        }
        processor.stop();
        return new ArrayList<>(Arrays.asList(results));
    }

    private Account requireAccount(String accountNumber)
            throws AccountNotFoundException {
        Account account = accounts.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountNumber);
        }
        return account;
    }

    private static void requirePositive(long amount) throws InvalidAmountException {
        if (!Validator.isValidAmount(amount)) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }

    private void append(String line) throws BankException {
        try {
            TransactionLog.append(logFile, line);
        } catch (IOException exception) {
            throw new BankException("Transaction succeeded but its log could not be written",
                    exception);
        }
    }
}
