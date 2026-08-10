package com.scs.start;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 集成测试基类：Testcontainers MySQL 8（与生产同引擎，项目中不引入 H2）。
 * 需要本机 Docker；无 Docker 时整类跳过（{@code disabledWithoutDocker}）。
 */
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@TestPropertySource(properties = {
        "scs.db.enabled=true",
        "spring.task.scheduling.enabled=false",
        "management.health.db.enabled=false",
        "print.config=false",
        "springdoc.swagger-ui.enabled=false"
})
public abstract class AbstractMysqlIT {

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("scs")
            .withUsername("test")
            .withPassword("test");
}
