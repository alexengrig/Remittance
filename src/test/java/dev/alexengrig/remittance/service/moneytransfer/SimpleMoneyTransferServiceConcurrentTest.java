package dev.alexengrig.remittance.service.moneytransfer;

import dev.alexengrig.remittance.domain.Account;
import dev.alexengrig.remittance.domain.AtomicAccount;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleMoneyTransferServiceConcurrentTest extends SimpleMoneyTransferServiceBaseTest {

    void testTransferMoneyInCircle(List<Account> accounts, List<Long> amounts) throws InterruptedException {
        assertEquals(accounts.size(), amounts.size(), "The size of accounts doesn't equal to the size of amounts");
        Balancer balancer = Balancer.of(accounts);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        int last = accounts.size() - 1;
        for (int i = 0; i < last; i++) {
            int index = i;
            executorService.execute(() -> {
                long amount = amounts.get(index);
                balancer.move(index, index + 1, amount);
                moneyTransferService.doTransfer(accounts.get(index), accounts.get(index + 1), amount);
            });
        }
        executorService.execute(() -> {
            long amount = amounts.get(last);
            balancer.move(last, 0, amount);
            moneyTransferService.doTransfer(accounts.get(last), accounts.get(0), amount);
        });
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            assertEquals(balancer.get(i), account.getBalance(), "The account balance is incorrect: " + account);
        }
    }

    @Test
    void should_transfer_money_in_circle_for_atomicAccounts() throws InterruptedException {
        testTransferMoneyInCircle(
                Arrays.asList(new AtomicAccount(1L, 100), new AtomicAccount(2L, 200), new AtomicAccount(3L, 300)),
                Arrays.asList(100L, 200L, 300L));
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
