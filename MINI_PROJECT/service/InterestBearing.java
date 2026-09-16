package service;

import model.Account;

public interface InterestBearing {
    double interestRate();

    default double yearlyInterest() {
        Account account = (Account) this;
        return account.getBalance() * interestRate() / 100.0;
    }
}