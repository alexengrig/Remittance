package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.repository.AccountRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class LockMoneyTransferService extends SimpleMoneyTransferService {

    private final LockService lockService;

    public LockMoneyTransferService(AccountRepository accountRepository, LockService lockService) {
        super(accountRepository);
        this.lockService = lockService;
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    protected void doTransfer(Account from, Account to, long amount) {
        lockService.runWithLock(from.getId(), to.getId(), () -> super.doTransfer(from, to, amount));
    }
}
