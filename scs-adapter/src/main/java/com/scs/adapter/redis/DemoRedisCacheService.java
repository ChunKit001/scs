package com.scs.adapter.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Redis Cache 演示：慢加载 + {@code @Cacheable}，用于对比首次/二次耗时。
 */
@Service
@ConditionalOnProperty(prefix = "scs.redis", name = "enabled", havingValue = "true")
@Slf4j
public class DemoRedisCacheService {

    private final AtomicInteger loadCount = new AtomicInteger();

    @Cacheable(cacheNames = RedisConfiguration.DEMO_CACHE, key = "#id")
    public String load(String id) {
        int n = loadCount.incrementAndGet();
        log.info("demo redis cache miss, id={}, loadCount={}", id, n);
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "cached-" + id;
    }

    @CacheEvict(cacheNames = RedisConfiguration.DEMO_CACHE, key = "#id")
    public void evict(String id) {
        log.info("demo redis cache evict, id={}", id);
    }

    public int loadCount() {
        return loadCount.get();
    }
}
