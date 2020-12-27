package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.MockitoAnnotationTest;
import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimpleMoneyTransferServiceTest implements MockitoAnnotationTest {

    AutoCloseable closeable;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    SimpleMoneyTransferService moneyTransferService;

    @Override
    public AutoCloseable getCloseable() {
        return closeable;
    }

    @Override
    public void setCloseable(AutoCloseable closeable) {
        this.closeable = closeable;
    }

    @Test
    void should_transfer_money_for_accounts() {
        Account from = new Account(1L, 500);
        Account to = new Account(2L, 500);
        int amount = 250;
        moneyTransferService.transfer(from, to, amount);
        assertEquals(1L, from.getId(), "The account id is incorrect");
        assertEquals(2L, to.getId(), "The account id is incorrect");
        assertEquals(250, from.getBalance(), "The account balance is incorrect");
        assertEquals(750, to.getBalance(), "The account balance is incorrect");
        verify(accountRepository).save(eq(from));
        verify(accountRepository).save(eq(to));
    }

    @Test
    void should_transfer_money_for_accounts_by_ids() {
        Account from = new Account(1L, 500);
        Account to = new Account(2L, 500);
        when(accountRepository.findById(from.getId())).thenReturn(Optional.of(from));
        when(accountRepository.findById(to.getId())).thenReturn(Optional.of(to));
        int amount = 250;
        moneyTransferService.transfer(from.getId(), to.getId(), amount);
        assertEquals(1L, from.getId(), "The account id is incorrect");
        assertEquals(2L, to.getId(), "The account id is incorrect");
        assertEquals(250, from.getBalance(), "The account balance is incorrect");
        assertEquals(750, to.getBalance(), "The account balance is incorrect");
        verify(accountRepository).findById(eq(from.getId()));
        verify(accountRepository).findById(eq(to.getId()));
        verify(accountRepository).save(eq(from));
        verify(accountRepository).save(eq(to));
    }
}