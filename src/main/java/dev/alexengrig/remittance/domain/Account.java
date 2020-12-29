package dev.alexengrig.remittance.domain;

public interface Account {
    long getId();

    long getBalance();

    void deposit(long amount);

    void withdraw(long amount);
}
