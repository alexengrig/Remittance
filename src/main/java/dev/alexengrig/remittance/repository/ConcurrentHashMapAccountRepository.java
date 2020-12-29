package dev.alexengrig.remittance.repository;

import dev.alexengrig.remittance.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConcurrentHashMapAccountRepository extends MapAccountRepository {

    @Override
    protected Map<Long, Account> createMap() {
        return new ConcurrentHashMap<>();
    }
}
