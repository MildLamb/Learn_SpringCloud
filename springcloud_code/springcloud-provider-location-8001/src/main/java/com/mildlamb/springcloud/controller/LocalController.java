package com.mildlamb.springcloud.controller;

import com.mildlamb.springcloud.pojo.Location;
import com.mildlamb.springcloud.service.LocalService;
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

    //这个接口可以获取到一些信息，得到具体的微服务
    @Autowired
    private DiscoveryClient client;

    @PostMapping("/local/add")
    public boolean addLocal(Location location){
        return localService.addLocation(location);
    }

    @GetMapping("/local/get/{lid}")
    public Location getLocalById(@PathVariable("lid") Integer localId){
        return localService.selectLocById(localId);
    }

    @GetMapping("/local/get")
    public List<Location> getLocals(){
        return localService.selectAllLoc();
    }


    //注册进去的微服务，获取一些信息
    @GetMapping("/local/discover")
    public Object discovery(){
        //获取微服务列表的清单
        List<String> services = client.getServices();
        System.out.println("Discovery ==> services:" + services);

        //得到一个具体的微服务信息
        List<ServiceInstance> instances = client.getInstances("springcloud-provider-location");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getHost() + "\t" +
                    instance.getPort() + "\t" +
                    instance.getUri() + "\t" +
                    instance.getServiceId() + "\t" +
                    instance.getMetadata() + "\t" +
                    instance.getScheme());
        }

        return this.client;
    }
}
