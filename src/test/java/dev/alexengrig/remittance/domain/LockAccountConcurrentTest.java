package dev.alexengrig.remittance.domain;

class LockAccountConcurrentTest extends AccountConcurrentTest {

    @Override
    Account createAccount(long balance) {
        return new LockAccount(1L, balance);
    }
}