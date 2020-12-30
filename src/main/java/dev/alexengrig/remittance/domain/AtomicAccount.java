package dev.alexengrig.remittance.domain;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicAccount extends BaseAccount {

    private final AtomicLong balance;

    public AtomicAccount(long id, long balance) {
        super(id);
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
