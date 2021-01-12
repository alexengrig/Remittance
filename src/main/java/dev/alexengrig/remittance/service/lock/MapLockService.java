package dev.alexengrig.remittance.service.lock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class MapLockService extends InMemoryLockService {

    private final Duration lockLiveDuration;
    private final Map<Object, Lock> locks;
    private final Map<Object, LocalDateTime> requestTimes;

    public MapLockService() {
        lockLiveDuration = Duration.of(60, ChronoUnit.SECONDS);
        locks = createLocksMap();
        requestTimes = createRequestTimesMap();
    }

    protected Map<Object, Lock> createLocksMap() {
        return new ConcurrentHashMap<>();
    }

    protected Map<Object, LocalDateTime> createRequestTimesMap() {
        return new ConcurrentHashMap<>();
    }

    @Override
    protected Lock getLock(Object key) {
        return locks.get(key);
    }

    @Override
    protected void saveLock(Object key, Lock lock) {
        locks.put(key, lock);
    }

    @Override
    protected void updateRequestTime(Object key, Lock lock) {
        requestTimes.put(key, LocalDateTime.now());
    }

    @Override
    protected void idle() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(150);
    }

    @Override
    public void shrink() {
        LocalDateTime now = LocalDateTime.now();
        requestTimes.forEach((key, dateTime) -> {
            if (Duration.between(dateTime, now).compareTo(lockLiveDuration) > 0) {
                locks.remove(key);
            }
        });
    }
}
