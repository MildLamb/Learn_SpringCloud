# Zuul使用
- 导入依赖
```xml
<dependencies>

    <!-- Zuul -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        <version>2.2.9.RELEASE</version>
    </dependency>

    <!-- hystrix监控页面依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        <version>2.2.9.RELEASE</version>
    </dependency>

    <!-- Hystrix -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        <version>2.2.9.RELEASE</version>
    </dependency>


    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>


    <!-- 实体类 -->
    <dependency>
        <groupId>org.mildlamb</groupId>
        <artifactId>springcloud-api</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <!-- web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
- 编写配置文件
```yml
server:
  port: 9527

spring:
  application:
    name: springcloud-zuul-geteway

eureka:
  client:
    service-url:
      defaultZone: http://eureka7002:7002/eureka/,http://eureka7001:7001/eureka/,http://eureka7003:7003/eureka/
  instance:
    instance-id: This is Zuul-9527
    prefer-ip-address: true

info:
  app.name: mildlamb-springcloud-zuul
  company.name: gnardada.com
```
- 启动类开启支持
```java
@SpringBootApplication
@EnableZuulProxy
public class ZuulApplication_9527 {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication_9527.class,args);
    }
}
```
