# Gateway使用
- 导入依赖
```xml
    <dependencies>

        <!-- Gateway
            springcloud的gateway使用的是webflux,默认使用netty,所以从依赖中排除 tomcat相关的依赖
         -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
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
    </dependencies>
```
- application.yml
```yml
server:
  port: 9527

spring:
  main:
    web-application-type: reactive
  application:
    name: springcloud-gateway
  # gateway 配置
  cloud:
    gateway:
      filter:
        rewrite-path:
          enabled: true
      routes:
        - id: service-provider-getNumber # 路由的id,没有规定规则但要求唯一,建议配合服务名
          #匹配后提供服务的路由地址
          uri: http://localhost:8001
          predicates:
            - Path=/api-gateway/** # 断言，路径相匹配的进行路由(注意**为通配符),允许什么样的路径访问
          filters:
            # 左边是你要输入的地址格式，有边是你会映射的地址格式
            # 例如 /api-gateway/local/get/4   ==>  /local/get/4
            # 这里要在上面的Path中开放访问权限
            # 组合起来就会实现   http://localhost:9527/api-gateway/local/get/4  映射为  http://localhost:8001/local/get/4
            - RewritePath=/api-gateway(?<segment>/?.*), $\{segment}
#        - id: service-provider-getInfo
#          uri: http://localhost:8001
#          predicates:
#            - Path=/provider/getInfo  #断言,路径相匹配的进行路由


eureka:
  client:
    service-url:
      defaultZone: http://eureka7002:7002/eureka/,http://eureka7001:7001/eureka/,http://eureka7003:7003/eureka/
  instance:
    instance-id: This is gateway-9527
    prefer-ip-address: true

info:
  app.name: mildlamb-springcloud-gateway
  company.name: gnardada.com
```
