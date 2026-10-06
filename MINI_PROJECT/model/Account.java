package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;
import service.InterestBearing;
import service.Transactable;

public abstract class Account implements Transactable, InterestBearing,
        Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @MaxLength(6)
    private final String accountNumber;
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;

    private static int counter = 1;

    private static String generateAccountNumber() {
        return String.format("AC%04d", counter++);
    }

    public Account(String ownerName, long balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    @Override
    public synchronized void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public synchronized boolean withdraw(long amount) {
        if (canWithdraw(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public double yearlyInterest() {
        return InterestBearing.super.yearlyInterest();
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Balance: ₹" + balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Account))
            return false;

        Account other = (Account) obj;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        String digits = accountNumber.replaceFirst("^AC", "");
        try {
            counter = Math.max(counter, Integer.parseInt(digits) + 1);
        } catch (NumberFormatException exception) {
            throw new IOException("Invalid serialized account number: "
                    + accountNumber, exception);
        }
    }
}