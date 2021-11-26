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
@Service
@FeignClient(value = "SPRINGCLOUD-PROVIDER-LOCATION")
public interface LocationClientService {
    @GetMapping("/customer/local/get/{lid}")
    Location selectById(@PathVariable("lid") Integer id);

    @GetMapping("/customer/local/all")
    List<Location> selectAll();

    @PostMapping("/customer/local/add")
    Boolean addLocation();
}
```
- 将feign模块的controller改为接口的方法调用
```java
@RestController
public class LocalController {
    @Autowired
    private RestTemplate restTemplate;  //提供多种便捷访问远程http服务的方法，简单的Restful服务模板

    @Autowired
    private LocationClientService locationClientService;


    @GetMapping("/customer/local/get/{lid}")
    public Location getLocal(@PathVariable("lid") Integer id){
        return locationClientService.selectById(id);
    }


    @GetMapping("/customer/local/all")
    public List<Location> getLocals(){
        return locationClientService.selectAll();
    }

    @PostMapping("/customer/local/add")
    public boolean add(Location location){
        return locationClientService.addLocation(location);
    }

}
```
