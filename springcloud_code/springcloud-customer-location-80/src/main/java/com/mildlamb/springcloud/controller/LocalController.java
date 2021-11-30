package com.mildlamb.springcloud.controller;

import com.mildlamb.springcloud.pojo.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
