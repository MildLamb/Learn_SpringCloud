# Ribbon
### ribbon是什么?  
- Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡的工具。
- 简单的说，Ribbon是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法，  
将NetFlix的中间层服务连接在一起。Ribbon的客户端组件提供一系列完整的配置项如:连接超时、重试等等。  
简单的说，就是在配置文件中列出LoadBalancer(简称LB∶负载均衡）后面所有的机器，Ribbon会自动的帮助  
你基于某种规则(如简单轮询，随机连接等等）去连接这些机器。我们也很容易使用Ribbon实现自定义的负载均衡算法  

### ribbon能干嘛?  
- LB，即负载均衡(Load Balance)，在微服务或分布式集群中经常用的一种应用。
- 负载均衡简单的说就是将用户的请求平摊的分配到多个服务上，从而达到系统的HA (高可用)。
- 常见的负载均衡软件有Nginx，Lvs等等
- dubbo、SpringCloud中均给我们提供了负载均衡，SpringCloud的负载均衡算法可以自定义负载均衡简单分类:
  - 负载均衡简单分类：
    - 集中式LB
      - 即在服务的消费方和提供方之间使用独立的LB设施，如Nginx，由该设施负责把访问请求通过某种策略转发至服务的提供方!
    - 进程式LB
      - 将LB逻辑集成到消费方，消费方从服务注册中心获知有哪些地址可用，然后自己再从这些地址中选出一个合适的服务器。
      - Ribbon就属于进程内LB，它只是一个类库，集成于消费方进程，消费方通过它来获取到服务提供方的地址

# Ribbon使用
- 客户端80导入额外依赖(eureka3.0以上的版本包含ribbon，再导ribbon会导致jar包冲突)
```xml
<!-- Ribbon客户端的负载均衡工具 -->
<!-- <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    <version>2.2.9.RELEASE</version>
</dependency> -->
<!-- Eureka Client 客户端要到Eureka中获取服务
  eureka3.0以上的版本包含ribbon，再导ribbon会导致jar包冲突 
-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
- 添加额外配置
```yml
# Eureka配置
eureka:
  client:
    register-with-eureka: false  # 不向eureka中注册自己
    service-url:
      defaultZone: http://eureka7002:7002/eureka/,http://eureka7001:7001/eureka/,http://eureka7003:7003/eureka/
```
- 修改一下RestTemplate
```java
@Configuration
public class ConfigBean {
    @Bean
    //配置负载均衡实现RestTemplate
    @LoadBalanced  //ribbon
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```
- 修改Controller，默认地址改为服务名称
```java
@RestController
public class LocalController {
    //理解：消费者，不应该有service层~
    //RestTemplate
    //restTemplate.xxxForObject：通过某种请求方式获取对象  由于消费者不应该有service，在这里就是通过请求的方式获取生产者提供的对象
    //(url,responseType)  url:请求的地址  responseType：返回值类型
    // 其他参数  get请求的请求参数以地址形式传输  post以参数类型传输 postForObject(url,request,responseType)
    @Autowired
    private RestTemplate restTemplate;  //提供多种便捷访问远程http服务的方法，简单的Restful服务模板

    //使用Ribbon的时候，路径不应该写死了，这个地址应该是一个变量，通过服务的id去查找
//    private static final String REST_UTL_PREFIX = "http://localhost:8001";
    private static final String REST_UTL_PREFIX = "http://SPRINGCLOUD-PROVIDER-LOCATION";


    @RequestMapping("/customer/local/get/{lid}")
    public Location getLocal(@PathVariable("lid") Integer id){
        return restTemplate.getForObject(REST_UTL_PREFIX + "/local/get/" + id,Location.class);
    }


    @RequestMapping("/customer/local/all")
    public List<Location> getLocals(){
        return restTemplate.getForObject(REST_UTL_PREFIX + "/local/get",List.class);
    }

    @RequestMapping("/customer/local/add")
    public boolean add(Location location){
        return restTemplate.postForObject(REST_UTL_PREFIX + "/local/add",location,Boolean.class);
    }

}
```
## 模拟负载均衡的环境
- 复制cloud01数据库，创建cloud02，cloud03
- 模仿提供者8001，创建8002，8003，修改对应提供者的提供数据的数据库
- 启动注册中心，启动提供者，启动消费者访问，就可以以轮询的负载均衡算法查询数据了

### 自定义负载均衡算法
