package dev.alexengrig.remittance.service.lock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class MapLockServiceTest {

    private static final Runnable DO_NOTHING = () -> {
    };

    @Test
    void should_runInLocks_incAndDec() throws InterruptedException {
        LockService lockService = new MapLockService(0);
        class Number {
            final long id;
            long value;

            Number(long id, long value) {
                this.id = id;
                this.value = value;
            }
        }
        BiFunction<Number, Number, Runnable> transfer = (left, right) -> () -> {
            try {
                lockService.runWithLock(left.id, right.id, () -> {
                    left.value--;
                    right.value++;
                });
            } catch (InterruptedException e) {
                fail();
            }
        };
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int count = 1_000_000;
        long expected = Long.MAX_VALUE - (count * 3);
        Number number0 = new Number(0, expected);
        Number number1 = new Number(1, expected);
        Number number2 = new Number(2, expected);
        for (int i = 0; i < count; i++) {
            executorService.execute(transfer.apply(number0, number1));
            executorService.execute(transfer.apply(number1, number2));
            executorService.execute(transfer.apply(number2, number0));
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            fail();
        }
        assertEquals(expected, number0.value);
        assertEquals(expected, number1.value);
        assertEquals(expected, number2.value);
    }

    @Test
    void should_shrink() throws InterruptedException {
        MapLockService mapLockService = new MapLockService(0);
        mapLockService.runWithLock(1, 2, DO_NOTHING);
        mapLockService.runWithLock(3, 4, DO_NOTHING);
        assertEquals(4, mapLockService.getNumberOfLocks());
        mapLockService.shrink();
        assertEquals(0, mapLockService.getNumberOfLocks());
        mapLockService.runWithLock(1, 2, DO_NOTHING);
        mapLockService.runWithLock(3, 4, DO_NOTHING);
        assertEquals(4, mapLockService.getNumberOfLocks());
    }

    @Test
    void should_not_shrink() throws InterruptedException {
        MapLockService mapLockService = new MapLockService(60);
        mapLockService.runWithLock(1, 2, DO_NOTHING);
        assertEquals(2, mapLockService.getNumberOfLocks());
        mapLockService.shrink();
        assertEquals(2, mapLockService.getNumberOfLocks());
    }

    @Test
    void should_throw_illegalArgument_for_equalKeys() {
        MapLockService mapLockService = new MapLockService(0);
        Executable task = () -> mapLockService.runWithLock(1, 1, DO_NOTHING);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, task);
        assertEquals("Equal objects: 1 = 1", exception.getMessage());
    }
}