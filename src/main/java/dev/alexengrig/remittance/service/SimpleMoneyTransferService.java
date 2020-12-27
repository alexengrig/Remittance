package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.exception.InsufficientFundsException;
import dev.alexengrig.remittance.exception.NotFoundAccountException;
import dev.alexengrig.remittance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SimpleMoneyTransferService implements MoneyTransferService {

    private final AccountRepository accountRepository;

    @Override
    public void transfer(long fromId, long toId, long amount) {
        requireDifferentAccounts(fromId, toId);
        requireValidAmount(amount);
        Account from = accountRepository.findById(fromId).orElseThrow(NotFoundAccountException::new);
        Account to = accountRepository.findById(toId).orElseThrow(NotFoundAccountException::new);
        doTransfer(from, to, amount);
    }

    @Override
    public void transfer(Account from, Account to, long amount) {
        Objects.requireNonNull(from, "The sender account must not be null");
        Objects.requireNonNull(to, "The recipient account must not be null");
        requireDifferentAccounts(from, to);
        requireValidAmount(amount);
        doTransfer(from, to, amount);
    }

    private void doTransfer(Account from, Account to, long amount) {
        if (from.getBalance() < amount) {
            throw new InsufficientFundsException();
        }
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
    }

    private void requireDifferentAccounts(Account from, Account to) {
        requireDifferentAccounts(from.getId(), to.getId());
    }

    private void requireDifferentAccounts(long fromId, long toId) {
        if (fromId == toId) {
            throw new IllegalArgumentException("The same account: " + fromId);
        }
    }

    private void requireValidAmount(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount must be positive and greater than zero: " + amount);
        }
    }
}
