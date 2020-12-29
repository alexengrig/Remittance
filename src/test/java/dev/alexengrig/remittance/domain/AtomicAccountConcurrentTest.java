package dev.alexengrig.remittance.domain;

class AtomicAccountConcurrentTest extends AccountConcurrentTest {

    @Override
    Account createAccount(long balance) {
        return new AtomicAccount(1L, balance);
    }
}