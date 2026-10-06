import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.InsufficientFundsException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import repository.Repository;
import service.BankService;
import util.StatePersister;

class BankServiceTest {
    @TempDir
    Path temporaryDirectory;

    private BankService newService(Repository<Account> repository) {
        Path logs = temporaryDirectory.resolve("logs");
        return new BankService(repository, logs.resolve("transactions.log"),
                logs, temporaryDirectory.resolve("report.txt"));
    }

    @Test
    void depositIncreasesBalance() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Asha", 1000, 0);
        repository.save(account);

        newService(repository).deposit(account.getAccountNumber(), 500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void withdrawalBeyondAvailableBalanceThrows() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Ravi", 100, 0);
        repository.save(account);

        assertThrows(InsufficientFundsException.class,
                () -> newService(repository).withdraw(
                        account.getAccountNumber(), 101));
    }

    @Test
    void concurrentDepositsKeepTheCorrectBalance() throws Exception {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Meera", 0, 0);
        repository.save(account);
        BankService service = newService(repository);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            List<Callable<Void>> deposits = new ArrayList<>();
            for (int index = 0; index < 400; index++) {
                deposits.add(() -> {
                    service.deposit(account.getAccountNumber(), 1);
                    return null;
                });
            }
            for (Future<Void> result : executor.invokeAll(deposits)) {
                result.get();
            }
        } finally {
            executor.shutdown();
        }

        assertEquals(400, account.getBalance());
    }

    @Test
    void savedAccountsLoadWithTheSameIdentityAndBalance() throws Exception {
        Account account = new SavingsAccount("Dev", 750, 0);
        Path file = temporaryDirectory.resolve("accounts.dat");

        StatePersister.save(new Account[] {account}, file);
        Account restored = StatePersister.load(file)[0];

        assertEquals(account.getAccountNumber(), restored.getAccountNumber());
        assertEquals(account.getBalance(), restored.getBalance());
    }
}
