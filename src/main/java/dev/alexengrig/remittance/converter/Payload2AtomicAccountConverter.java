package dev.alexengrig.remittance.converter;

import dev.alexengrig.remittance.domain.AtomicAccount;
import dev.alexengrig.remittance.payload.AccountPayload;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Payload2AtomicAccountConverter implements Converter<AccountPayload, AtomicAccount> {

    @Override
    public AtomicAccount convert(AccountPayload source) {
        return new AtomicAccount(source.getId(), source.getBalance());
    }
}
