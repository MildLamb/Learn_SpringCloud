## SpringCloud config 分布式配置
### 概述
- **分布式系统面临的--配置文件的问题**  
微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现大量的服务，  
由于每个服务都需要必要的配置信息才能运行，所以一套集中式的，动态的配置管理设施是必不可少的。SpringCloud  
提供了ConfigServer来解决这个问题，我们每一个微服务自己带着一个application.yml，那上百的的配置文件要修  
改起来，岂不是要发疯!

- **什么是SpringCloud config分布式配置中心**  
  - Spring Cloud Config 为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所  
有环节提供了一个中心化的外部配置  
  - Spring Cloud Config 分为服务端和客户端两部分;  
  - 服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密  
，解密信息等访问接口。  
  - 客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载  
配置信息。配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理。并且可以通过git客户端  
工具来方便的管理和访问配置内容。

## springcloud-config使用
### 服务端
- 导入依赖
```xml
<dependencies>
    <!-- config server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <!-- web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
- 编写配置(连接github的配置，要求提供用户名和密码)
```yml
server:
  port: 3344
spring:
  application:
    name: springcloud-config-server
  # 连接远程仓库
  cloud:
    config:
      server:
        git:
          uri: https://github.com/MildLamb/Learn_Git_Config.git
          username: 1902524390@qq.com
          password: W2kindred
        default-label: main
```
- 启动类开启支持
```java
@SpringBootApplication
@EnableConfigServer
public class Config_Server_3344 {
    public static void main(String[] args) {
        SpringApplication.run(Config_Server_3344.class,args);
    }
}
```
- 访问远程配置
  - 官网提供了多种访问的方式
```bash
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.propertie
```
  - 例如我们的远程配置可以这样访问
    - http://localhost:3344/application/test/main
    - http://localhost:3344/application-dev.yml

### 客户端
- 导入依赖
```xml
<dependencies>
    <!-- config-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <!-- web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
- 编写两个配置文件 bootstrap.yml和application.yml
```yml
# 系统级别的配置
spring:
  cloud:
    config:
      uri: http://localhost:3344
      name: config-client   # 需要通过git读取的资源名称，不需要加后缀
      profile: dev  # 需要获取哪种生产环境
      label: main  # 从哪一个分支拿
```
```yml
# 用户级别的配置
spring:
  application:
    name: springcloud-config-client-3355
```
- 编写一个controller来获取远程的配置
```java
@RestController
public class ConfigClientController {
    // 注入的位置来自远程github上的配置文件
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServer;
    @Value("${server.port}")
    private String port;

    @RequestMapping("/config")
    public String MyConfig(){
        return "application-name:" + applicationName + "\r\n" +
                "server-url:" + eurekaServer + "\r\n" +
                "server-port:"  + port;
    }
}
```
- 启动类测试
```java
@SpringBootApplication
public class Config_Client_3355 {
    public static void main(String[] args) {
        SpringApplication.run(Config_Client_3355.class,args);
    }
}
```

## 流程
config客户端  -->  config服务端  -->  github远程配置文件

### 测试出现的一些问题  
SpringCloud 2020.0.2版本的配置中心的客户端报No spring.config.import property has been defined  
在springcloud 2020.0.2版本中把bootstrap的相关依赖从spring-cloud-starter-config中移除了，导致报错.
解决方案：  
- 添加bootstrap依赖
```xml
<!-- bootstrap.yml -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
