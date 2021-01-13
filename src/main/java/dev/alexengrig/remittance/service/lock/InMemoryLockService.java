package dev.alexengrig.remittance.service.lock;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class InMemoryLockService implements LockService {

    @Override
    public void runWithLock(Object left, Object right, Runnable runnable) throws InterruptedException {
        requireDifferent(left, right);
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

    private void requireDifferent(Object left, Object right) {
        if (Objects.equals(left, right)) {
            throw new IllegalArgumentException("Equal objects: " + left + " = " + right);
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
