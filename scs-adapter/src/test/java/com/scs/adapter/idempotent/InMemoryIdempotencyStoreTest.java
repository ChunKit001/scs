package com.scs.adapter.idempotent;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryIdempotencyStoreTest {

    private final InMemoryIdempotencyStore store = new InMemoryIdempotencyStore();

    @Test
    void beginCompleteAndReplay() {
        String key = "k1";
        assertThat(store.tryBegin(key, 60)).isTrue();
        assertThat(store.tryBegin(key, 60)).isFalse();

        store.complete(key, "done", 60);
        assertThat(store.get(key)).isPresent();
        assertThat(store.get(key).get().getStatus()).isEqualTo(IdempotencyRecord.Status.COMPLETED);
        assertThat(store.get(key).get().getResponse()).isEqualTo("done");
    }

    @Test
    void removeAllowsRetry() {
        String key = "k2";
        assertThat(store.tryBegin(key, 60)).isTrue();
        store.remove(key);
        assertThat(store.tryBegin(key, 60)).isTrue();
    }
}
