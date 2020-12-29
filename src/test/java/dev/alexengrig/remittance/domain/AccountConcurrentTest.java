package dev.alexengrig.remittance.domain;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

abstract class AccountConcurrentTest {

    abstract Account createAccount(long balance);

    @Test
    void should_deposit() throws InterruptedException {
        long total = 100_000;
        Account account = createAccount(0);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (long i = 0; i < total; i++) {
            executorService.execute(() -> account.deposit(1));
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        assertEquals(total, account.getBalance(), "The account balance is incorrect");
    }

    @Test
    void should_withdraw() throws InterruptedException {
        long total = 100_000;
        Account account = createAccount(total);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (long i = 0; i < total; i++) {
            executorService.execute(() -> account.withdraw(1));
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        assertEquals(0, account.getBalance(), "The account balance is incorrect");
    }

    @Test
    void should_deposit_and_withdraw() throws InterruptedException {
        long total = 100_000;
        Account account = createAccount(total);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (long i = 0, count = total / 2; i < count; i++) {
            executorService.execute(() -> account.deposit(1));
            executorService.execute(() -> account.withdraw(1));
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        assertEquals(total, account.getBalance(), "The account balance is incorrect");
    }
}
