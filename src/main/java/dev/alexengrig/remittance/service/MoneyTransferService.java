package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.domain.Account;

public interface MoneyTransferService {

    void transfer(long fromId, long toId, long amount);

    default void transfer(Account from, Account to, long amount) {
        transfer(from.getId(), to.getId(), amount);
    }
}
