package dev.alexengrig.remittance.repository;

import dev.alexengrig.remittance.domain.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(long accountId);

    void update(Account account);
}
