package dev.alexengrig.remittance.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class SyncAccount implements Account {

    @Getter(AccessLevel.NONE)
    private final Object balanceLock = new Object();

    private final long id;
    private volatile long balance;

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
