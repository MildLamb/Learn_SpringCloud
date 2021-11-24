# Eureka服务注册与发现
- 什么是Eureka?
  - Netfilx在设计Eureka时，遵循的就是AP原则
  - Eureka是Netfilx的一个子模块，也是核心模块之一。Eureka是一个基于REST的服务，用于定位服务，以实现云端中间层服务发现和故障转移
  - Eureka包含两个组件：Eureka Server和Eureka Client
  - Eureka Server提供服务注册，各个节点启动后，会在EurekaServer中进行注册，这样Eureka Server中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到
  - Eureka Client是一个Java客户端，用于简化EurekaServer的交互，客户端同时也具备一个内置的，使用轮询负载算法的负载均衡器。在启动应用后，将会向EurekaServer发送心跳(默认周期为30s)。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除掉(默认周期为90s)

- 三大角色
  - Eureka Server：提供服务注册与发现
  - Service Provider：将自身服务注册到Eureka中，从而使消费方能够找到
  - Service Consumer：服务消费方从Eureka中获取注册服务列表，从而找到消费服务


# 配置Eureka Server
- 导入依赖
```xml
  <!-- 导入Eureka包 -->
  <dependencies>
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
      </dependency>
  </dependencies>
```
- 编写配置application.yml
```yml
server:
  port: 7001

# Eureka配置
eureka:
  instance:
    hostname: localhost  # Eureka服务端的实例名称
  client:
    register-with-eureka: false  # 表示是否向eureka注册中心注册自己，注册中心不需要注册自己
    fetch-registry: false # fetch-registry如果为false，则表示自己为注册中心
    service-url:  # 服务监控页面
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```
- 编写主启动类，开启EurekaServer服务
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer_7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer_7001.class,args);
    }
}
```
# 将provider-8001配置为服务提供者，向Eureke注册中心注册
- 导入额外依赖
```xml
<!-- Eureka Client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
- application.yml中添加额外配置
```yml
# Eureka配置
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka/  # Eureka注册中心的注册地址
  instance:
    instance-id: 8001 is springcloud-provider   # 修改注册到Eureka后的默认描述
```
- 启动类添加额外注解@EnableEurekaClient
```java
@SpringBootApplication
@EnableEurekaClient
public class LocalProvider_8001 {
    public static void main(String[] args) {
        SpringApplication.run(LocalProvider_8001.class,args);
    }
}
```
