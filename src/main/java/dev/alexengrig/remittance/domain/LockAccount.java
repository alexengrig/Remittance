package dev.alexengrig.remittance.domain;

import lombok.Getter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockAccount extends BaseAccount {

    private final Lock balanceLock = new ReentrantLock();

    @Getter
    private volatile long balance;

    public LockAccount(long id, long balance) {
        super(id);
        this.balance = balance;
    }

    @Override
    public void deposit(long amount) {
        balanceLock.lock();
        try {
            balance += amount;
        } finally {
            balanceLock.unlock();
        }
    }

    @Override
    public void withdraw(long amount) {
        balanceLock.lock();
        try {
            balance -= amount;
        } finally {
            balanceLock.unlock();
        }
    }
}
