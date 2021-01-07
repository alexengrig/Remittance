package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.repository.AccountRepository;

public class LockMoneyTransferService extends SimpleMoneyTransferService {

    public LockMoneyTransferService(AccountRepository accountRepository) {
        super(accountRepository);
    }
}
