package com.scs.adapter.idempotent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等接口：请求头必须带 X-Idempotency-Key。
 * 脚手架默认用本地内存存储；有 Redis 时可替换 {@link IdempotencyStore} 实现。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /** 成功结果缓存秒数 */
    long ttlSeconds() default 300;
}
