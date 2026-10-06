package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import model.Account;

public class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> accounts;

    public TransactionProcessor() {
        this(new ConcurrentHashMap<>());
    }

    public TransactionProcessor(ConcurrentHashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public int accountCount() {
        return accounts.size();
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.DAYS)) {
            // Keep waiting until every submitted transaction has finished.
        }
    }

    public static boolean transfer(Account source, Account destination, long amount) {
        if (source == destination) {
            return false;
        }

        Account firstLock = source.getAccountNumber().compareTo(
                destination.getAccountNumber()) < 0 ? source : destination;
        Account secondLock = firstLock == source ? destination : source;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (!source.withdraw(amount)) {
                    return false;
                }
                destination.deposit(amount);
                return true;
            }
        }
    }
}