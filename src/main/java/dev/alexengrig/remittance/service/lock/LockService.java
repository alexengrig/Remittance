package dev.alexengrig.remittance.service.lock;

public interface LockService {

    void runWithLock(Object left, Object right, Runnable runnable) throws InterruptedException;
}
