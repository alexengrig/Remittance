package dev.alexengrig.remittance.domain;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicAccount implements Account {
    @Getter
    private final long id;
    private final AtomicLong balance;

    public AtomicAccount(long id, long balance) {
        this.id = id;
        this.balance = new AtomicLong(balance);
    }

    @Override
    public long getBalance() {
        return balance.get();
    }

    @Override
    public void deposit(long amount) {
        balance.addAndGet(amount);
    }

    @Override
    public void withdraw(long amount) {
        balance.addAndGet(-amount);
    }
}
