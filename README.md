本项目旨在编写一个：
符合DDD规范的
开箱即用的
安全的
高性能的
spring脚手架

功能清单
1.jdk21-协程替代原有tomcat线程池
2.check-dependency
#3.代码混淆
#4.jacoco-代码覆盖率

定时任务配置化
自定义filter
自定义interceptor
启动时输出全部变量
不同环境使用不同日志配置
异步调用
配置引用与配置随机
#websocket
#mqtt

###### QA:

报错直接以errMessage为什么不用CatchLogAspect字段返回到前端 不安全