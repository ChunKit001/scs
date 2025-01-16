# 项目目标：

符合DDD规范的、开箱即用的、安全的、高性能的java后端脚手架 \
scs=springboot+cola+scaffold

## 环境：
jdk21

## 功能清单：

1. jdk21-协程替代原有tomcat线程池 VirtualThreadConfig
2. @Async-协程替代原有线程池 ThreadPoolConfig
3. 日志配置、定时任务配置化 application-dev.yml
4. check-dependency 安全依赖管理 
   ./mvnw org.owasp:dependency-check-maven:check
5. 启动时输出全部变量 PrintConfig
6. 自定义filter
7. 自定义interceptor
8. 接口签名校验
9. 全局异常捕获
10. spring优雅关闭
11. Bean实例化执行任务
12. i18n
13. validation参数校验 demo
14. surefire单元测试并行化
15. 自定义打包（bin、config、lib结构）
16. mapstruct框架

# todo
#jacoco-代码覆盖率 \
#配置文件加密 \
#代码混淆 \
#自定义打包（graalvm） \
#自定义打包（docker镜像）

# 参考:

https://github.com/alibaba/COLA
https://github.com/zhangkaitao/es.git

# 主类
com.scs.Application
