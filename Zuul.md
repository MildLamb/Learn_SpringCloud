## Zuul路由网关(新版的springcloud好像不支持Zuul了，用新版的springcloud，Zuul没有成功运行，我专用了Gateway)
- 什么是Zuul?
Zuul包含了对请求的路由和过滤两个最主要的功能：  
其中路由功能负责将外部请求转发到具体的微服务实例上，是实现外部访问同一入口的基础，而过滤器功能  
则负责对请求的处理过程进行干预，是实现请求校验，服务聚合等功能的基础。Zuul和Eureka进行整合，将   
Zuul自身注册为Eureka服务治理下的应用，同时从Eureka中获得其他微服务的消息，也即以后的访问微服务  
都是通过Zuul跳转后获得。  

注意：Zuul服务最终还是会注册进Eureka  
提供：代理 + 路由 + 过滤  三大功能  

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

# Zuul配置
zuul:
  routes:
    mylocation.serviceId: springcloud-provider-location
    mylocation.path: /mylocation/**
  # 不允许通过服务名去访问服务，只能用上面自定义的路径去访问
  ignored-services: springcloud-provider-location
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
