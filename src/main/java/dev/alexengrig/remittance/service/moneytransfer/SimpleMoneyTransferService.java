package dev.alexengrig.remittance.service.moneytransfer;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class SimpleMoneyTransferService extends BaseMoneyTransferService {

    public SimpleMoneyTransferService(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    protected void doTransfer(Account from, Account to, long amount) {
        from.withdraw(amount);
        to.deposit(amount);
    }
}
