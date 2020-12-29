package dev.alexengrig.remittance.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleAccountTest {

    @Test
    void should_deposit() {
        Account account = new SimpleAccount(1L, 0);
        account.deposit(100);
        assertEquals(100, account.getBalance(), "The account balance is incorrect");
    }

    @Test
    void should_withdraw() {
        Account account = new SimpleAccount(1L, 100);
        account.withdraw(100);
        assertEquals(0, account.getBalance(), "The account balance is incorrect");
    }
}