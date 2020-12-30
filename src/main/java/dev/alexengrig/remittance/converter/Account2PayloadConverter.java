package dev.alexengrig.remittance.converter;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.payload.AccountPayload;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Account2PayloadConverter implements Converter<Account, AccountPayload> {

    @Override
    public AccountPayload convert(Account source) {
        return new AccountPayload(source.getId(), source.getBalance());
    }
}
