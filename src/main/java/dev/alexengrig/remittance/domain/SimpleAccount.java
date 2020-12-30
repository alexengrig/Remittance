package dev.alexengrig.remittance.domain;

import lombok.Getter;

public class SimpleAccount extends BaseAccount {

    @Getter
    private long balance;

    public SimpleAccount(long id, long balance) {
        super(id);
        this.balance = balance;
    }

    @Override
    public void deposit(long amount) {
        balance += amount;
    }

    @Override
    public void withdraw(long amount) {
        balance -= amount;
    }
}
