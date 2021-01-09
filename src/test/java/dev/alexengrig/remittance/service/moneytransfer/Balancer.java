package dev.alexengrig.remittance.service.moneytransfer;

import dev.alexengrig.remittance.domain.Account;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Balancer {

    private final List<AtomicLong> balances;

    private Balancer(List<AtomicLong> balances) {
        this.balances = new CopyOnWriteArrayList<>(balances);
    }

    static Balancer of(List<Account> accounts) {
        return new Balancer(accounts.stream()
                .map(Account::getBalance)
                .map(AtomicLong::new)
                .collect(Collectors.toList()));
    }

    void move(int from, int to, long amount) {
        balances.get(from).addAndGet(-amount);
        balances.get(to).addAndGet(amount);
    }

    long get(int index) {
        return balances.get(index).get();
    }
}
