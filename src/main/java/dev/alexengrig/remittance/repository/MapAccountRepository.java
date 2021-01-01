package dev.alexengrig.remittance.repository;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.payload.AccountPayload;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
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
        Path path = Paths.get(filename);
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(path))) {
            @SuppressWarnings("unchecked")
            Collection<AccountPayload> payloads = (Collection<AccountPayload>) input.readObject();
            accountById.putAll(payloads.stream()
                    .map(this::mapToAccount)
                    .collect(Collectors.toMap(Account::getId, Function.identity())));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to pull data from file: " + filename, e);
        }
    }

    @Override
    public void pushData() {
        Path path = Paths.get(filename);
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(path))) {
            Collection<AccountPayload> accounts = accountById.values().stream()
                    .map(accountPayloadConverter::convert)
                    .collect(Collectors.toList());
            output.writeObject(accounts);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to push data from file: " + filename, e);
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
