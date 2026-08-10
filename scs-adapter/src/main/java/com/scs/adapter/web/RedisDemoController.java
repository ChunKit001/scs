package com.scs.adapter.web;

import com.alibaba.cola.dto.SingleResponse;
import com.scs.adapter.redis.DemoRedisCacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Redis 可选演示：{@code scs.redis.enabled=true} 时启用。默认不连 Redis。
 */
@ConditionalOnProperty(prefix = "scs.redis", name = "enabled", havingValue = "true")
@Tag(name = "RedisDemo", description = "可选 Redis Cache / 连通性演示（需 scs.redis.enabled=true）")
@RestController
@RequestMapping("demo/redis")
@RequiredArgsConstructor
public class RedisDemoController {

    private final StringRedisTemplate stringRedisTemplate;
    private final DemoRedisCacheService demoRedisCacheService;

    @Operation(summary = "写入并读回临时 key，验证 Redis 连通")
    @GetMapping("ping")
    public SingleResponse<String> ping(@RequestParam(defaultValue = "scs") String value) {
        String key = "scs:demo:ping";
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));
        return SingleResponse.of(stringRedisTemplate.opsForValue().get(key));
    }

    @Operation(summary = "Spring Cache → Redis：对比首次 miss 与二次 hit 的耗时 / loadCount")
    @GetMapping("cache")
    public SingleResponse<Map<String, Object>> cache(@RequestParam(defaultValue = "1") String id) {
        long started = System.nanoTime();
        String value = demoRedisCacheService.load(id);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("value", value);
        body.put("elapsedMs", elapsedMs);
        body.put("loadCount", demoRedisCacheService.loadCount());
        return SingleResponse.of(body);
    }

    @Operation(summary = "驱逐指定 id 的缓存")
    @DeleteMapping("cache")
    public SingleResponse<String> evict(@RequestParam(defaultValue = "1") String id) {
        demoRedisCacheService.evict(id);
        return SingleResponse.of("evicted-" + id);
    }
}
