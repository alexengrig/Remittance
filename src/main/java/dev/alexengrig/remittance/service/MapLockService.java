package dev.alexengrig.remittance.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class MapLockService extends InMemoryLockService {

    private final Map<Object, Lock> locks;

    public MapLockService() {
        locks = createMap();
    }

    protected abstract Map<Object, Lock> createMap();

    @Override
    protected Lock getLock(Object key) {
        Lock lock = locks.get(key);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        locks.put(key, lock);
        return lock;
    }

    @Override
    protected void idle() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(150);
    }
}
