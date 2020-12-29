package dev.alexengrig.remittance.domain;

class SyncAccountConcurrentTest extends AccountConcurrentTest {

    @Override
    Account createAccount(long balance) {
        return new SyncAccount(1L, balance);
    }
}