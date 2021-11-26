# Feign使用
- 创建一个新模块springcloud-consumer-dept-feign，复制80消费者模块
- 在springcloud-api模块和刚刚复制的模块中添加feign依赖
```xml
<!-- feign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
