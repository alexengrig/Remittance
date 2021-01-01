package dev.alexengrig.remittance.repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class InMemoryAccountRepository implements AccountRepository {

    @PostConstruct
    protected abstract void pullData();

    @PreDestroy
    protected abstract void pushData();
}
