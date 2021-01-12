package dev.alexengrig.remittance.service.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class InMemoryLockService implements LockService {

    @Override
    public <T> void runWithLock(T left, T right, Runnable runnable) throws InterruptedException {
        Lock leftLock = obtainLock(left);
        Lock rightLock = obtainLock(right);
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

    private Lock obtainLock(Object key) {
        Lock lock = getLock(key);
        if (lock == null) {
            lock = createLock(key);
            saveLock(key, lock);
        }
        updateRequestTime(key, lock);
        return lock;
    }

    protected abstract Lock getLock(Object key);

    protected Lock createLock(Object key) {
        return new ReentrantLock();
    }

    protected abstract void saveLock(Object key, Lock lock);

    protected abstract void updateRequestTime(Object key, Lock lock);

    protected abstract void idle() throws InterruptedException;

    public abstract void shrink();
}
