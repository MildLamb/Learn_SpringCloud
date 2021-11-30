package com.mildlamb.springcloud.controller;

import com.mildlamb.springcloud.pojo.Location;
import com.mildlamb.springcloud.service.LocalService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
