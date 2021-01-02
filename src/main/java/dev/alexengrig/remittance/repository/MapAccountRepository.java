package dev.alexengrig.remittance.repository;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.payload.AccountPayload;
import dev.alexengrig.remittance.util.SerializationUtil;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MapAccountRepository extends InMemoryAccountRepository {

    private final String filename;
    private final Converter<Account, AccountPayload> accountPayloadConverter;
    private final Map<Long, Account> accountById;

    public MapAccountRepository(String filename, Converter<Account, AccountPayload> accountPayloadConverter) {
        this.filename = filename;
        this.accountPayloadConverter = accountPayloadConverter;
        accountById = createMap();
    }

    protected abstract Map<Long, Account> createMap();

    protected abstract Account mapToAccount(AccountPayload payload);

    @Override
    public void pullData() {
        try {
            ArrayList<AccountPayload> payloads = SerializationUtil.deserializeFromFile(filename);
            accountById.putAll(payloads.stream()
                    .map(this::mapToAccount)
                    .collect(Collectors.toMap(Account::getId, Function.identity())));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to pull data from file: " + filename, e);
        }
    }

    @Override
    public void pushData() {
        try {
            ArrayList<AccountPayload> payloads = accountById.values().stream()
                    .map(accountPayloadConverter::convert)
                    .collect(Collectors.toCollection(ArrayList::new));
            SerializationUtil.serializeToFile(payloads, filename);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to push data to file: " + filename, e);
        }
    }

    @Override
    public Optional<Account> findById(long accountId) {
        return Optional.ofNullable(accountById.get(accountId));
    }

    @Override
    public void update(Account account) {
        accountById.put(account.getId(), account);
    }
}
