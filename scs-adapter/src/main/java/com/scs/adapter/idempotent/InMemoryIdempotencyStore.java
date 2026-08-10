package com.scs.adapter.idempotent;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单机内存幂等存储（脚手架默认，无需 Redis）。
 */
@Component
public class InMemoryIdempotencyStore implements IdempotencyStore {

    private final ConcurrentHashMap<String, IdempotencyRecord> store = new ConcurrentHashMap<>();

    @Override
    public Optional<IdempotencyRecord> get(String key) {
        IdempotencyRecord record = store.get(key);
        if (record == null) {
            return Optional.empty();
        }
        if (record.expired(System.currentTimeMillis())) {
            store.remove(key, record);
            return Optional.empty();
        }
        return Optional.of(record);
    }

    @Override
    public boolean tryBegin(String key, long ttlSeconds) {
        long expireAt = System.currentTimeMillis() + ttlSeconds * 1000;
        IdempotencyRecord beginning = new IdempotencyRecord(IdempotencyRecord.Status.IN_PROGRESS, null, expireAt);
        IdempotencyRecord existing = store.putIfAbsent(key, beginning);
        if (existing == null) {
            return true;
        }
        if (existing.expired(System.currentTimeMillis())) {
            return store.replace(key, existing, beginning);
        }
        return false;
    }

    @Override
    public void complete(String key, Object response, long ttlSeconds) {
        long expireAt = System.currentTimeMillis() + ttlSeconds * 1000;
        store.put(key, new IdempotencyRecord(IdempotencyRecord.Status.COMPLETED, response, expireAt));
    }

    @Override
    public void remove(String key) {
        store.remove(key);
    }
}
