package dev.alexengrig.remittance.service;

import java.util.concurrent.locks.Lock;

public abstract class InMemoryLockService implements LockService {

    @Override
    public <T> void runWithLock(T left, T right, Runnable runnable) throws InterruptedException {
        Lock leftLock = getLock(left);
        Lock rightLock = getLock(right);
        while (true) {
            if (leftLock.tryLock()) {
                if (rightLock.tryLock()) {
                    break;
                }
                leftLock.unlock();
            }
            idle();
        }
        try {
            runnable.run();
        } finally {
            leftLock.unlock();
            rightLock.unlock();
        }
    }

    protected abstract Lock getLock(Object key);

    protected abstract void idle() throws InterruptedException;
}
