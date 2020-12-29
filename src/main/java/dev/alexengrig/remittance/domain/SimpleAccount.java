package dev.alexengrig.remittance.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleAccount implements Account {
    private final long id;
    private long balance;

    @Override
    public void deposit(long amount) {
        balance += amount;
    }

    @Override
    public void withdraw(long amount) {
        balance -= amount;
    }
}
