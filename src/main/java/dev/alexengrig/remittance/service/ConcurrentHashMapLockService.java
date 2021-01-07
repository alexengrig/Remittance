package dev.alexengrig.remittance.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Service
public class ConcurrentHashMapLockService extends MapLockService {

    @Override
    protected Map<Object, Lock> createMap() {
        return new ConcurrentHashMap<>();
    }
}
