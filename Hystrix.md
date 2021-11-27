# Hystrix
## 什么是Hystrix?
- Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免地会调用失败，比如超时，异常等，
Hystrix能保证在一个依赖出问题地情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统地弹性。  
- “断路器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器地故障监控(类似熔断保险丝)，**向调用方返回一个服务预  
期的，可处理的备选响应(FallBack)，而不是长时间的等待或者抛出调用方法无法处理的异常，这样就可以保证服务调用方的线程  
不会被长时间，不必要的占用**，从而避免了故障在分布式系统中的蔓延，乃至雪崩。

## 服务雪崩
- 多个服务之间调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其他的微服务，这就是所谓的“扇出”，如果扇出的
链路上某个微服务的调用响应时间过长或不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，所谓的“雪崩效应”。  

## Hystrix能干嘛
- 服务降级
- 服务熔断
- 服务限流
- 接近实时的监控

### 服务熔断
熔断机制是对应雪崩效应的一种微服务链路保护机制。  
当扇出链路的某个微服务不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。当检测到  
该节点微服务调用响应正常后恢复调用链路。在SpringCloud框架里熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况，当失败  
的调用到一定阈值，缺省是5秒内20次调用失败就会启动熔断机制。熔断机制的注解是@HystrixCommand。 


### 自己理解的熔断和降级
- 熔断：由于系统发生了某种错误，为了防止雪崩，使原本的服务采用了其他备用手段维持服务链路的正常运行
- 降级：为了满足某个系统的高访问量，以牺牲其他服务为代价，关闭其他服务，集中处理高访问的服务


# Hystrix使用
### 服务熔断
- 导入依赖
```xml
 <!-- Hystrix -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    <version>2.2.9.RELEASE</version>
</dependency>
```
- 修改controller方法，使方法出现异常后，调用备用方案
```java
@RestController
public class LocalController {
    @Autowired
    private LocalService localService;

    @GetMapping("/local/get/{lid}")
    @HystrixCommand(fallbackMethod = "hystrixGetLocation")
    public Location getLocation(@PathVariable("lid") Integer id){
        Location location = localService.selectLocById(id);
        if (location == null){
            throw new RuntimeException("id => " + id + "不存在该用户");
        }
        return location;
    }

    // 我们想出现异常后调用备选方案
    public Location hystrixGetLocation(Integer id){
        Location location = new Location();
        location.setLocalId(id);
        location.setLocalName("不存在该用户 @By Hystrix");
        location.setDbSource("没有该数据库  @By Hystrix");
        return location;
    }
}
```
- 启动类开启熔断支持
```java
@SpringBootApplication
@EnableEurekaClient  //在服务启动后自动注册到Eureka中
@EnableDiscoveryClient  //服务发现
@EnableHystrix
public class LocalProvider_hystrix_8001 {
    public static void main(String[] args) {
        SpringApplication.run(LocalProvider_hystrix_8001.class,args);
    }
}
```
## 服务降级
- springcloud-api模块中，添加一个类实现FallbackFactory接口重写create方法
```java
//降级
@Component
public class LocationClientServiceFallbackFactory implements FallbackFactory {
    //返回值是提供的服务接口
    public LocationClientService create(Throwable cause) {
        return new LocationClientService() {
            public boolean addLocal(Location location) {
                return false;
            }

            //降级操作，返回一个带有错误信息的需要的返回类
            public Location getLocalById(Integer localId) {
                Location location = new Location();
                location.setLocalId(localId);
                location.setLocalName("不存在该用户 @By 服务降级，该服务已被降级关闭");
                location.setDbSource("没有该数据库  @By 服务降级，该服务已被降级关闭");
                return location;
            }

            public List<Location> getLocals() {
                return null;
            }
        };
    }
}
```
- 服务接口处，@FeignClient添加fallbackFactory属性与降级服务关联
```java
@FeignClient(value = "SPRINGCLOUD-PROVIDER-LOCATION",fallbackFactory = LocationClientServiceFallbackFactory.class)
public interface LocationClientService {

    @PostMapping("/local/add")
    public boolean addLocal(Location location);

    @GetMapping("/local/get/{lid}")
    public Location getLocalById(@PathVariable("lid") Integer localId);

    @GetMapping("/local/get")
    public List<Location> getLocals();
}
```
