package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {
    private final ConcurrentHashMap<String, Account> accounts =
            new ConcurrentHashMap<>();
    private final List<String> history = new ArrayList<>();

    public Account add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        Account previous = accounts.putIfAbsent(account.getAccountNumber(), account);
        if (previous != null) {
            throw new IllegalArgumentException("Account already exists: "
                    + account.getAccountNumber());
        }
        return account;
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void addHistory(String entry) {
        synchronized (history) {
            history.add(entry);
        }
    }

    public List<String> getHistory() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> result = new ArrayList<>(accounts.values());
        result.sort(Comparator.comparingLong(Account::getBalance).reversed()
                .thenComparing(Account::getAccountNumber));
        return result;
    }

    public ConcurrentHashMap<String, Account> concurrentMap() {
        return accounts;
    }
}
