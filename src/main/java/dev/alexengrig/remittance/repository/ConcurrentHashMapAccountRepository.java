package dev.alexengrig.remittance.repository;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.domain.AtomicAccount;
import dev.alexengrig.remittance.payload.AccountPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConcurrentHashMapAccountRepository extends MapAccountRepository {

    private final Converter<AccountPayload, AtomicAccount> atomicAccountConverter;

    public ConcurrentHashMapAccountRepository(
            @Value("${application.data.filename:data.ser}") String filename,
            Converter<Account, AccountPayload> accountPayloadConverter,
            Converter<AccountPayload, AtomicAccount> atomicAccountConverter) {
        super(filename, accountPayloadConverter);
        this.atomicAccountConverter = atomicAccountConverter;
    }

    @Override
    protected Map<Long, Account> createMap() {
        return new ConcurrentHashMap<>();
    }

    @Override
    protected Account mapToAccount(AccountPayload payload) {
        return atomicAccountConverter.convert(payload);
    }
}
