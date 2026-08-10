package com.scs.start.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 可选数据库：{@code scs.db.enabled=true}（或 {@code SCS_DB_ENABLED=true}）时启用。
 * 默认关闭，脚手架无 MySQL 也能启动；Customer 落库与 Flyway 仅在开启后可用。
 */
@Configuration
@ConditionalOnProperty(prefix = "scs.db", name = "enabled", havingValue = "true")
@Import({
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        FlywayAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class
})
@MapperScan("com.scs.infra")
public class DbConfiguration {
}
