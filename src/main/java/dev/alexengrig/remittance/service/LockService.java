package dev.alexengrig.remittance.service;

public interface LockService {

    <T> void runWithLock(T left, T right, Runnable runnable) throws InterruptedException;
}
