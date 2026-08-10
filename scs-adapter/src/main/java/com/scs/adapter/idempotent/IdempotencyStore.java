package com.scs.adapter.idempotent;

import java.util.Optional;

public interface IdempotencyStore {

    Optional<IdempotencyRecord> get(String key);

    boolean tryBegin(String key, long ttlSeconds);

    void complete(String key, Object response, long ttlSeconds);

    void remove(String key);
}
