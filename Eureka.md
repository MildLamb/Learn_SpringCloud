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
# Eureka配置，
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka/  # Eureka注册中心的注册地址
  instance:
    instance-id: 8001 is springcloud-provider   # 修改注册到Eureka后的默认描述
#    # 配置使用主机名注册服务
#    hostname: node1
#    # 优先使用IP地址方式进行注册服务
#    prefer-ip-address: true
#    # 配置使用指定IP
#    ip-address: 127.0.0.1


# 新版的actuator还是新版的springboot默认连/info都不开放了好像，所以要开启/info的访问
management:
  endpoints:
    web:
      exposure:
        include: "*"

# 需要actuator依赖
# 完善监控信息
info:
  app.name: MildLamb-SpringCloud
  company.name: Wild-Wolf
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
## 如果想在类中获取一些注册的微服务的信息可以这样
- 注入一个类 org.springframework.cloud.client.discovery.DiscoveryClient 这个包下的DiscoveryClient接口
```java
//这个接口可以获取到一些信息，得到具体的微服务
    @Autowired
    private DiscoveryClient client;
```
- 获取你想要的微服务信息
```java
//注册进去的微服务，获取一些信息
@GetMapping("/local/discover")
public Object discovery(){
    //获取微服务列表的清单
    List<String> services = client.getServices();
    System.out.println("Discovery ==> services:" + services);

    //得到一个具体的微服务信息
    List<ServiceInstance> instances = client.getInstances("springcloud-provider-location");  //这个getInstances参数是你配置的spring.application.name的值
    for (ServiceInstance instance : instances) {
        System.out.println(instance.getHost() + "\t" +
                instance.getPort() + "\t" +
                instance.getUri() + "\t" +
                instance.getServiceId());
    }

    return this.client;
}
```
# 搭建伪Eureka集群
- 创建eureka-7002和eureka-7003 和 7001一样
- 使7001，7002，7003产生关联
- 这里需要修改一下系统的hosts文件来伪实现一下 C:\Windows\System32\drivers\etc
- 由于是系统文件，我们直接打开是无法修改的
  1. 来到C:\Windows\System32\drivers\etc，点击左上角文件，以管理员身份运行PowerShell
  2. 输入cmd
  3. 输入notepad hosts
  4. 接下来就可以修改hosts文件了
  5. 多复制几个127.0.0.1  后面写名字
  6. 保存
- 在eureka server的yml配置文件中绑定其他集群成员
```yml
server:
  port: 7001

# Eureka配置
eureka:
  instance:
    hostname: eureka7001  # Eureka服务端的实例名称
  client:
    # 当我们设置服务为注册中心时，需要关闭eureka.client.fetch-registry与eureka.client.register-with-eureka，
    # 在做注册中心集群的时候，register-with-eureka必须打开，因为需要进行相互注册，不然副本无法可用
    register-with-eureka: true  # 表示是否向eureka注册中心注册自己，注册中心不需要注册自己
    fetch-registry: false # fetch-registry如果为false，则表示自己为注册中心
    service-url:  # 服务监控页面
      # 单机：defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      # defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/  #提供者提供服务 来注册的地址
      # 集群需要关联其他集群成员
      defaultZone: http://eureka7002:7002/eureka/,http://eureka7003:7003/eureka/
```
- 服务提供者，向集群注册服务
```yml
# Eureka配置，
eureka:
  client:
    service-url:
      defaultZone: http://eureka7002:7002/eureka/,http://eureka7001:7001/eureka/,http://eureka7003:7003/eureka/
```
