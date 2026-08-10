# SCS

Spring Boot + COLA 的 Java 后端脚手架（**S**pring Boot + **C**OLA + **S**caffold）。

面向公司大项目中的 **DDD / 微服务节点**：统一约定、可演示、可验证；限流与边缘鉴权交给 Gateway，本仓库聚焦节点内能力。

| 项 | 说明 |
|----|------|
| JDK | 21（虚拟线程） |
| Spring Boot | 3.4.x |
| 主类 | `com.scs.start.Application` |
| 默认端口 | `58080` |

模块职责见 [§2](#2-模块职责)；新功能怎么写见 [§4 功能开发流程](#4-功能开发流程从-api-到-db)（按 Customer 抄一遍即可）。

---

## 1. 快速开始

```bash
# 默认不连 MySQL / Redis，可直接起（Demo 接口可用）
./mvnw -pl scs-start -am spring-boot:run

# 需要 Customer 落库时：
# export SCS_DB_ENABLED=true
# export DB_URL=jdbc:mysql://localhost:3306/test
# export DB_USERNAME=root
# export DB_PASSWORD=root
```

常用文档与探针（非 prod）：

- Swagger UI：http://localhost:58080/swagger-ui.html  
- Actuator：`curl -u scs:scs-change-me http://localhost:58080/actuator/health`

---

## 2. 模块职责

COLA 按「依赖方向」拆模块：**外层可以依赖内层，内层不依赖外层实现**。  
Maven 依赖关系（箭头表示「依赖」）：

```text
scs-start
   └── scs-adapter
          └── scs-app
                 ├── scs-client
                 └── scs-infra
                        └── scs-domain
                               └── scs-client
```

运行时请求穿过的逻辑层次（与上图不完全同一方向）：

```text
adapter → app → domain(gateway 接口) → infra(gateway 实现) → DB
              ↗ client（契约：DTO / Cmd / ServiceI）
```

| 模块 | 一句话 | 放什么 | 不放什么 |
|------|--------|--------|----------|
| **scs-client** | 对外 API 契约包 | `*ServiceI`、Cmd/Qry、DTO、错误码、事件 | Spring Bean、SQL、Controller |
| **scs-adapter** | 适配入口（HTTP 等） | Controller、Filter、Interceptor、Security、幂等切面、Redis 演示 | 领域规则、直接 Mapper |
| **scs-app** | 应用服务 / 用例 | `*ServiceImpl`、`*CmdExe` / `*QryExe`、编排与校验 | 表结构、MyBatis XML |
| **scs-domain** | 领域核心 | 实体、领域服务、**Gateway 接口** | 框架注解实现、DO、SQL |
| **scs-infra** | 基础设施 | `*GatewayImpl`、Mapper/DO、Flyway、外部系统适配 | HTTP API、业务编排 |
| **scs-start** | 启动与组装 | `Application`、全局配置、`application-*.yml`、打包脚本、IT | 业务功能代码（尽量薄） |

### 2.1 `scs-client` — 契约（可被其它服务依赖）

- **职责：** 定义「这个节点对外承诺什么」，尽量稳定、少依赖。  
- **典型包：** `com.scs.client.api`、`dto`、`dto.data`、`dto.event`  
- **例子：** `CustomerServiceI`、`CustomerAddCmd`、`CustomerDTO`、`ErrorCode`  
- **注意：** 只有接口和数据类；**没有** `@Service` 实现。其它微服务若要调本服务，理论上只依赖本 jar（再配合 RPC/Feign 等，脚手架未内置）。

### 2.2 `scs-adapter` — 接入适配

- **职责：** 把外部协议（HTTP / 未来 MQ 等）转成对 `*ServiceI` 的调用；横切能力也多在这里。  
- **典型内容：** `web/*Controller`、`filter`、`interceptor`、`config`（Security / OpenAPI）、`idempotent`、`redis` 演示  
- **依赖：** `scs-app`（从而带上整条业务链）  
- **例子：** `CustomerController` 只注入 `CustomerServiceI`，组 Cmd/Qry 后下传。

### 2.3 `scs-app` — 用例编排

- **职责：** 实现 `*ServiceI`；一个用例一个 Exe（命令写、查询读）。  
- **典型包：** `com.scs.app.<聚合>`、`executor`、`executor.query`  
- **依赖：** `scs-client` + `scs-infra`（Spring 注入 Gateway 实现；代码里应面向 `domain` 的 Gateway **接口**编程）  
- **例子：** `CustomerServiceImpl` → `CustomerAddCmdExe` / `CustomerListByNameQryExe`

### 2.4 `scs-domain` — 领域

- **职责：** 业务含义与规则；通过 Gateway **接口**声明「我需要什么持久化/外部能力」，不关心 MySQL 还是 Mock。  
- **典型包：** `com.scs.domain.<聚合>`、`gateway`、`domainservice`  
- **依赖：** 可依赖 `scs-client`（错误码等）；**不要**依赖 `scs-infra` / `scs-adapter`  
- **例子：** `Customer` 实体、`CustomerGateway` 接口

### 2.5 `scs-infra` — 基础设施

- **职责：** 实现 Gateway；说话「表怎么建、SQL 怎么写、外部系统怎么调」。  
- **典型内容：** `*GatewayImpl`、`*Mapper`、`*DO`、`db/migration/V*.sql`、可选 `mybatis/*.xml`  
- **依赖：** `scs-domain`（实现其接口）+ MyBatis-Plus / Flyway / JDBC 驱动  
- **例子：** `CustomerGatewayImpl` + `CustomerMapper` + `V1__create_customer.sql`  
- **约定：** 单表优先 MyBatis-Plus `BaseMapper`；复杂连表再写 XML。

### 2.6 `scs-start` — 启动模块

- **职责：** 唯一可执行入口；组装配置与发行包，本身尽量不含业务。  
- **典型内容：** `Application`、`config/*`（校验、MyBatis-Plus 扫描等）、`application.yml` / `application-*.yml`、`bin/start.sh`、IT  
- **依赖：** `scs-adapter`（拉起全部业务 Bean）

根目录 `pom.xml`：版本与插件（Enforcer / JaCoCo / SpotBugs / ProGuard 等）统一管理，不是业务模块。

---

## 3. 能力一览

按职责分组（细节见后文对应章节）。

**运行时**

- JDK 21 虚拟线程（Tomcat / `@Async`）
- 优雅关闭、定时任务与日志可配置
- MDC `traceId`（请求头 `X-Trace-Id` / `X-Request-Id`，日志与响应回写）
- Filter：`TraceIdFilter`；Interceptor：响应头 `X-SCS-Interceptor: true`
- 全局异常 + i18n；参数校验（fail-fast）

**API 约定**

- COLA `Response` / `SingleResponse` / `MultiResponse` / `PageResponse`
- 错误码 `S_` / `P_` / `B_`（文案 key = errCode）
- 分页 `PageQuery`；幂等 `@Idempotent`（默认内存存储）

**数据与集成**

- Flyway 迁移（`scs-infra/.../db/migration`；需 `SCS_DB_ENABLED=true`）
- Customer 真读写：`POST/GET /customer`（需开 DB；Cmd → Gateway → MyBatis-Plus）
- 可选 Redis Cache（`SCS_REDIS_ENABLED=true`，默认关）
- **默认不强制 DB / Redis**：脚手架可空库启动，只跑 Demo 等非持久化能力
- MapStruct 对象转换演示

**可观测与文档**

- Actuator（non-prod + HTTP Basic；prod 关闭）
- springdoc OpenAPI（non-prod；prod 关闭）

**工程化**

- 测试：Surefire 并行；MockMvc 活文档 IT；JaCoCo 门禁（默认 ≥10%）
- 质量：Enforcer 默认开；Checkstyle / SpotBugs / OWASP 按 profile
- 发布：`bin` + `config` + `lib` 瘦包（`start.sh` 使用 `-cp`）

**明确不做（交给网关 / 业务自建）**

- 应用内限流、签名校验空壳、XSS Filter 空壳、本地+Redis 双缓存

---

## 4. 功能开发流程（从 API 到 DB）

面向不熟 DDD/COLA 的同学：新能力按 **Customer** 抄，不要发明第二套分层。  
模块各干什么见 [§2](#2-模块职责)。示例：要实现功能 **A**、落库表 **`a`**。

### 4.1 先记住三句话

1. **对外契约在 `scs-client`**（DTO / Cmd / Qry / `*ServiceI`），别的模块依赖接口，不依赖实现。  
2. **领域只认自己的模型 + Gateway 接口**；真正查库在 `scs-infra`。  
3. **Controller 只调 `*ServiceI`**，不调 Mapper、不写 SQL。

```text
HTTP → Adapter(Controller)
         → Client(*ServiceI)          ← 只暴露接口
              → App(*ServiceImpl + CmdExe/QryExe)
                   → Domain(*Gateway 接口 + 实体)
                        → Infra(*GatewayImpl + Mapper/DO + Flyway)
                             → DB
```

| 模块 | 你放什么 | 角色（详见 [§2](#2-模块职责)） |
|------|----------|------|
| `scs-client` | `AServiceI`、`AAddCmd`、`AXxxQry`、`ADTO`、错误码 | **对外 API 契约** |
| `scs-adapter` | `AController` | HTTP 入口 |
| `scs-app` | `AServiceImpl`、`AAddCmdExe`、`AXxxQryExe` | 用例编排 |
| `scs-domain` | `A` 实体、`AGateway` **接口** | 业务规则 |
| `scs-infra` | `AGatewayImpl`、`AMapper`、`ADO`、Flyway | 持久化实现 |
| `scs-start` | 一般不用改 | 启动与全局配置 |

三种模型不要混用：

| 模型 | 包 | 用途 |
|------|-----|------|
| `ADTO` | `client.dto.data` | 进出 HTTP / 对外 |
| `A` | `domain.a` | 领域逻辑 |
| `ADO` | `infra.a` | 表映射（MyBatis-Plus） |

App 里 DTO ↔ 实体；Infra 里 实体 ↔ DO。

### 4.2 要暴露哪些 interface、谁来 implement

| 接口（定义在） | 实现（写在） | 谁依赖接口 |
|----------------|--------------|------------|
| `com.scs.client.api.AServiceI` | `com.scs.app.a.AServiceImpl`（`@Service`） | Controller、其它应用服务、IT |
| `com.scs.domain.a.gateway.AGateway` | `com.scs.infra.a.AGatewayImpl`（`@Component`） | 仅 App（CmdExe / QryExe） |
| `com.scs.infra.a.AMapper`（可 `extends BaseMapper<ADO>`） | MyBatis-Plus 运行时代理；复杂 SQL 在 XML | 仅 `AGatewayImpl` |

**不要**让 App/Domain 直接注入 `AMapper`。  
**不要**使用 MyBatis-Plus 的 `IService` / `ServiceImpl`（会把业务和表操作糊在一起）。

### 4.3 推荐落地顺序（清单）

按依赖从里到外做，编译更顺；也可对照现有 Customer 文件改名复制。

#### ① 表结构（infra / Flyway）

- 新增：`scs-infra/src/main/resources/db/migration/V{n}__create_a.sql`  
- 只追加新版本，不改已发布的 `V1`…  
- 本地重启后 Flyway 自动迁移（可用 `FLYWAY_ENABLED=false` 临时关掉）

#### ② 持久化（infra）

```text
scs-infra/.../com/scs/infra/a/
  ADO.java              @TableName("a") + @TableId
  AMapper.java          extends BaseMapper<ADO>   // 单表够用
  AGatewayImpl.java     implements AGateway
```

- 单表：`selectById` / `insert` / `Wrappers.lambdaQuery()` 等。  
- 复杂连表：在 `AMapper` 声明方法 + `scs-infra/src/main/resources/mybatis/a-mapper.xml`（namespace = Mapper 全名）。

同时在 domain 先写好端口（见下），`AGatewayImpl` 才能编译。

#### ③ 领域端口与实体（domain）

```text
scs-domain/.../com/scs/domain/a/
  A.java                      // 实体 + 简单业务方法
  gateway/AGateway.java       // 仓储端口：save / findXxx / existsXxx …
```

Gateway 方法用 **领域语言**（`save(A)`），不要出现 `ADO`、ResultSet。

#### ④ 对外契约（client）

```text
scs-client/.../com/scs/client/
  api/AServiceI.java
  dto/AAddCmd.java              // 写操作命令
  dto/AListByXxxQry.java        // 读操作查询（可 extends cola Query）
  dto/data/ADTO.java
  dto/data/ErrorCode.java       // 按需加 B_A_* / P_*
```

`AServiceI` 返回 COLA：`Response` / `SingleResponse` / `MultiResponse` / `PageResponse`。

#### ⑤ 用例（app）

```text
scs-app/.../com/scs/app/a/
  AServiceImpl.java                 implements AServiceI
  executor/AAddCmdExe.java          // 写：校验 → 领域对象 → gateway
  executor/query/AListByXxxQryExe.java
```

`AServiceImpl` 只做转发；逻辑放在 `*Exe`。Exe **只依赖** `AGateway`（和本领域对象），不依赖 Controller / Mapper。

#### ⑥ HTTP（adapter）

```text
scs-adapter/.../com/scs/adapter/web/AController.java
```

- `@RequestMapping("a")`，注入 `AServiceI`  
- `POST` 接 `@Valid AAddCmd`；`GET` 组 `AListByXxxQry`  
- 不写业务、不碰 DB

#### ⑦ 自测

```bash
curl -X POST http://localhost:58080/a -H "Content-Type: application/json" -d '{...}'
curl "http://localhost:58080/a?..."
```

有复杂路径再补 `scs-start` 的 MockMvc IT（可参考现有 Customer / Demo IT）。

### 4.4 对照抄：Customer 现成文件

| 步骤 | 参考路径 |
|------|----------|
| Controller | `scs-adapter/.../web/CustomerController.java` |
| ServiceI / Cmd / Qry / DTO | `scs-client/.../api/CustomerServiceI.java`，`dto/CustomerAddCmd.java`，`dto/CustomerListByNameQry.java`，`dto/data/CustomerDTO.java` |
| ServiceImpl / Exe | `scs-app/.../customer/CustomerServiceImpl.java`，`executor/CustomerAddCmdExe.java`，`executor/query/CustomerListByNameQryExe.java` |
| 实体 / Gateway | `scs-domain/.../customer/Customer.java`，`gateway/CustomerGateway.java` |
| GatewayImpl / Mapper / DO | `scs-infra/.../customer/CustomerGatewayImpl.java`，`CustomerMapper.java`，`CustomerDO.java` |
| 建表 | `scs-infra/.../db/migration/V1__create_customer.sql` |

### 4.5 常见误区

| 别这样做 | 应这样做 |
|----------|----------|
| Controller 里 `@Autowired CustomerMapper` | Controller → `*ServiceI` |
| Domain 里引 MyBatis / Spring Data | Domain 只保留 `*Gateway` 接口 |
| App 继承 MP 的 `ServiceImpl<Mapper,DO>` | App 用 CmdExe + 注入 `*Gateway` |
| DTO / 实体 / DO 共用一个类 | 三套模型，边界处转换 |
| 改旧 Flyway 脚本 | 永远新增 `V{n+1}__...sql` |
| 每张表先写一堆 XML | 单表优先 `BaseMapper`；连表再 XML |

---

## 5. 环境与配置

### 5.1 环境对照

| 环境 | Spring Profile | 配置文件 | 数据库 | Actuator | Swagger | 怎么启动 |
|------|----------------|----------|--------|----------|---------|----------|
| 本地开发 | `dev`（默认） | `application-dev.yml` | MySQL，默认库名 `test` | 开（含 env/loggers） | 开 | 见下「本地」 |
| **测试环境（部署）** | `test` | `application-test.yml` | MySQL，默认库名 `scs_test` | 开 | 开 | 见下「测试环境」 |
| 生产 | `prod` | `application-prod.yml` | MySQL（务必用环境变量） | **关** | **关** | 见下「生产」 |

说明：

- 运行时环境一律 **MySQL**（与生产同引擎），**不引入 H2**。  
- Redis 连接写在各环境 yml；默认 `scs.redis.enabled=false`（无 Redis 也能起），需要时 `SCS_REDIS_ENABLED=true`。  
- 数据库同理：默认 `scs.db.enabled=false`；`SCS_DB_ENABLED=true` 后才启用 DataSource / Flyway / Customer。  
- 集成测试用 **Testcontainers** 临时拉起 MySQL 8 容器（需本机 Docker），见 `AbstractMysqlIT`；无 Docker 时相关 IT 自动跳过。  
- 公共项在 `application.yml`；Flyway 脚本在 `scs-infra/.../db/migration`。

### 5.2 各环境启动示例

**本地（dev）**

```bash
export DB_URL=jdbc:mysql://localhost:3306/test
export DB_USERNAME=root
export DB_PASSWORD=root
./mvnw -pl scs-start -am spring-boot:run
# 或解压包后：./bin/start.sh          # 默认 dev
```

**测试环境（test）**

```bash
export SPRING_PROFILES_ACTIVE=test
export DB_URL=jdbc:mysql://测试库主机:3306/scs_test
export DB_USERNAME=...
export DB_PASSWORD=...
./bin/start.sh test
# 需要 Redis Cache 时：
# export SCS_REDIS_ENABLED=true
# export REDIS_HOST=...
```

**生产（prod）**

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://生产库:3306/...
export DB_USERNAME=...
export DB_PASSWORD=...          # prod 默认口令为空，必须显式配置
# export SCS_REDIS_ENABLED=true
# export REDIS_HOST=...
./bin/start.sh prod
```

**集成测试（Testcontainers MySQL，需 Docker）**

```bash
./mvnw test
```

### 5.3 环境变量

| 变量 | 用途 | 备注 |
|------|------|------|
| `SCS_DB_ENABLED` | 是否启用数据源 / Flyway / Customer | 默认 `false`；`true` 时才连 MySQL |
| `DB_URL` / `DB_USERNAME` / `DB_PASSWORD` / `DB_DRIVER` | 数据源 | 仅 `SCS_DB_ENABLED=true` 时使用 |
| `FLYWAY_ENABLED` | 是否执行迁移 | 默认 `true`（且需已开 DB）；临时关闭：`false` |
| `SCS_SECURITY_USER` / `SCS_SECURITY_PASSWORD` | Actuator Basic | 默认 `scs` / `scs-change-me` |
| `SCS_REDIS_ENABLED` | 是否启用 Redis Cache | 默认 `false`；`true` 时才会连 Redis / 注册演示接口 |
| `REDIS_HOST` / `REDIS_PORT` / `REDIS_PASSWORD` | Redis 连接 | 写在各环境 yml；仅 `SCS_REDIS_ENABLED=true` 时使用 |
| `SPRING_PROFILES_ACTIVE` | 激活的 profile | `dev` / `test` / `prod` |
| `print.config=true` | 启动打印全部配置 | 勿在生产开启 |

---

## 6. 演示接口

### 6.1 Customer（落库，需 `SCS_DB_ENABLED=true`）

```bash
# export SCS_DB_ENABLED=true
curl -X POST http://localhost:58080/customer -H "Content-Type: application/json" \
  -d '{"customerDTO":{"companyName":"Acme","source":"WEB"}}'
curl "http://localhost:58080/customer?name=Acme"
```

链路：见 [§4](#4-功能开发流程从-api-到-db)。简述：`CustomerController` → `CustomerServiceI` → Cmd/QryExe → `CustomerGateway` → MyBatis-Plus → `customer`。  
单表用 `BaseMapper` / Wrapper；复杂连表在 Mapper 接口 + `scs-infra/.../mybatis/*.xml`。

### 6.2 Demo 约定

| 接口 | 说明 |
|------|------|
| `GET /demo/base-s` | 成功体；带 `X-SCS-Interceptor: true` |
| `GET /demo/base-f` | 业务错误 + i18n（`Accept-Language`） |
| `GET /demo/page?pageIndex=1&pageSize=2` | 分页 |
| `POST /demo/valid` | 参数校验 |
| `POST /demo/idempotent` + 头 `X-Idempotency-Key` | 幂等 |
| `GET /demo/trace` | 回显 MDC traceId |
| `GET /demo/virtual-thread` | 是否虚拟线程 |

### 6.3 Redis Cache（需 `SCS_REDIS_ENABLED=true`）

```bash
# export SCS_REDIS_ENABLED=true
curl "http://localhost:58080/demo/redis/ping"
curl "http://localhost:58080/demo/redis/cache?id=1"   # miss
curl "http://localhost:58080/demo/redis/cache?id=1"   # hit，loadCount 不变
curl -X DELETE "http://localhost:58080/demo/redis/cache?id=1"
```

### 6.4 其它

- Trace：可带 `X-Trace-Id`；未带则服务端生成并回写。  
- MapStruct：`scs-app` 中 `PojoConvertUtil` + `MapperStructService`。  
- Flyway：只追加 `V*.sql`；需先 `SCS_DB_ENABLED=true`；临时关闭迁移用 `FLYWAY_ENABLED=false`。

---

## 7. 构建、测试与质量

可选质量工具（Checkstyle / SpotBugs / OWASP / ProGuard）统一在**根 `pom.xml` 的 profiles**，子模块不要再抄一份。规则文件也在仓库根：`spotbugs-exclude.xml`、`proguard.cfg`。

### 7.1 常用命令

```bash
./mvnw test                          # IT（Testcontainers MySQL，需 Docker）
./mvnw clean verify                  # 含 JaCoCo 门禁
./mvnw validate                      # Enforcer（JDK21 / 禁旧依赖）
./mvnw validate -P checkstyle
./mvnw verify -P spotbugs            # SpotBugs + FindSecBugs
./mvnw verify -P owasp               # 依赖 CVE（慢，建议 NVD_API_KEY）
./mvnw clean package -DskipTests -T 4
./mvnw clean package -DskipTests -T 4 -P proguard   # 混淆（可选；scs-client 默认跳过）
```

### 7.2 测试说明

- IT：继承 `AbstractMysqlIT`（Testcontainers MySQL）；无 Docker 则跳过  
- 单测：`PageQueryTest`、`InMemoryIdempotencyStoreTest`、`DemoAopTest`、`DemoInterceptorTest`（不依赖库）  
- JaCoCo：默认行覆盖率 ≥ `0.10`；`client/domain/infra/start` 暂 `jacoco.check.skip`；业务项目建议调到 `0.60`  
  - 报告：`*/target/site/jacoco/index.html`  
  - 跳过：`-Djacoco.skip=true` 或 `-Djacoco.check.skip=true`

### 7.3 发行包

产物：`scs-start/target/scs-start-*-package.tar.gz`（`bin/` + `config/` + `lib/`）。

```bash
export SPRING_PROFILES_ACTIVE=prod   # 可选；也可用 ./bin/start.sh prod
./bin/start.sh
./bin/stop.sh
```

`start.sh` 使用 `java -cp config:lib/*`（**不要** `java -jar`，否则外置 `config` 中的 i18n / logback 不在 classpath）。  
主 jar 仅排除 `application*.yml`；`i18n` 与 `logback-spring.xml` 打进 jar，并复制到 `config/` 便于覆盖。

---

## 8. 规划中

- 配置加密、Docker / GraalVM 打包  
- 接口签名 / XSS（按业务自建，脚手架不提供空壳）  
- 全链路 OTel / Jaeger（单服务已有 MDC traceId）  
- 业务统一鉴权（当前业务 API `permitAll`，仅 Actuator Basic）

---

## 9. 参考

- [COLA](https://github.com/alibaba/COLA)  
- [es](https://github.com/zhangkaitao/es.git)
