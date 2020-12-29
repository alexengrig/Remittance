package dev.alexengrig.remittance.repository;

import dev.alexengrig.remittance.domain.Account;

import java.util.Map;
import java.util.Optional;

public abstract class MapAccountRepository implements InMemoryAccountRepository {

    private final Map<Long, Account> accountById;

    public MapAccountRepository() {
        accountById = createMap();
    }

    protected abstract Map<Long, Account> createMap();

    @Override
    public Optional<Account> findById(long accountId) {
        return Optional.ofNullable(accountById.get(accountId));
    }

    @Override
    public void update(Account account) {
        accountById.put(account.getId(), account);
    }
}
