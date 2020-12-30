package dev.alexengrig.remittance.domain;

import lombok.Getter;

public class SyncAccount extends BaseAccount {

    private final Object balanceLock = new Object();

    @Getter
    private volatile long balance;

    public SyncAccount(long id, long balance) {
        super(id);
        this.balance = balance;
    }

    @Override
    public void deposit(long amount) {
        synchronized (balanceLock) {
            balance += amount;
        }
    }

    @Override
    public void withdraw(long amount) {
        synchronized (balanceLock) {
            balance -= amount;
        }
    }
}
