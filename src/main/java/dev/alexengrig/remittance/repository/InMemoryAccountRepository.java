package dev.alexengrig.remittance.repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public interface InMemoryAccountRepository extends AccountRepository {

    @PostConstruct
    void pullData();

    @PreDestroy
    void pushData();
}
