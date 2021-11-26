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
- 在springcloud-api模块中添加service
```java
//value：微服务的id，你要从哪个微服务拿
/**
    注意这里绑定的请求地址是服务提供者提供的请求地址
    相当于FeignClient和服务提供者进行绑定
 */
@FeignClient(value = "SPRINGCLOUD-PROVIDER-LOCATION")
public interface LocationClientService {

    @PostMapping("/local/add")
    public boolean addLocal(Location location);

    @GetMapping("/local/get/{lid}")
    public Location getLocalById(@PathVariable("lid") Integer localId);

    @GetMapping("/local/get")
    public List<Location> getLocals();
}
```
- 将feign模块的controller改为接口的方法调用,消费者的链接可以自定义
```java
@RestController
public class LocalController {

    @Autowired
    private LocationClientService service;


    @RequestMapping("/feign/local/get/{lid}")
    public Location getLocal(@PathVariable("lid") Integer id){
        return service.getLocalById(id);
    }


    @RequestMapping("/feign/local/all")
    public List<Location> getLocals(){
        return service.getLocals();
    }

    @RequestMapping("/feign/local/add")
    public boolean add(Location location){
        return service.addLocal(location);
    }

}
```
- 启动类添加@EnableFeignClients注解
```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FeignCustomer_80 {
    public static void main(String[] args) {
        SpringApplication.run(FeignCustomer_80.class,args);
    }
}
```
