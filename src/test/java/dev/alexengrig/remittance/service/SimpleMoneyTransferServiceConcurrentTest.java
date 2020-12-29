package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.domain.AtomicAccount;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleMoneyTransferServiceConcurrentTest extends SimpleMoneyTransferServiceBaseTest {

    @Test
    void should_transfer_money_in_circle_for_atomicAccounts() throws InterruptedException {
        Account account1 = new AtomicAccount(1L, 100);
        Account account2 = new AtomicAccount(2L, 200);
        Account account3 = new AtomicAccount(3L, 300);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(() -> moneyTransferService.doTransfer(account1, account2, 100));
        executorService.execute(() -> moneyTransferService.doTransfer(account2, account3, 200));
        executorService.execute(() -> moneyTransferService.doTransfer(account3, account1, 300));
        executorService.shutdown();
        if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        assertEquals(300, account1.getBalance(), "The account1 balance is incorrect");
        assertEquals(100, account2.getBalance(), "The account2 balance is incorrect");
        assertEquals(200, account3.getBalance(), "The account3 balance is incorrect");
    }

    @Test
    void should_transfer_money_in_one_for_atomicAccounts() throws InterruptedException {
        Account account1 = new AtomicAccount(1L, 100);
        Account account2 = new AtomicAccount(2L, 200);
        Account account3 = new AtomicAccount(3L, 300);
        Account account4 = new AtomicAccount(4L, 0);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(() -> moneyTransferService.doTransfer(account1, account4, 100));
        executorService.execute(() -> moneyTransferService.doTransfer(account2, account4, 200));
        executorService.execute(() -> moneyTransferService.doTransfer(account3, account4, 300));
        executorService.shutdown();
        if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        assertEquals(0, account1.getBalance(), "The account1 balance is incorrect");
        assertEquals(0, account2.getBalance(), "The account2 balance is incorrect");
        assertEquals(0, account3.getBalance(), "The account3 balance is incorrect");
        assertEquals(600, account4.getBalance(), "The account4 balance is incorrect");
    }
}
