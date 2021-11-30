package com.mildlamb.springcloud.service;

import com.mildlamb.springcloud.pojo.Location;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//value：微服务的id，你要从哪个微服务拿
/**
    注意这里绑定的请求地址是服务提供者提供的请求地址
    相当于FeignClient和服务提供者进行绑定
 */
@FeignClient(value = "SPRINGCLOUD-PROVIDER-LOCATION",fallbackFactory = LocationClientServiceFallbackFactory.class)
public interface LocationClientService {

    @PostMapping("/local/add")
    public boolean addLocal(Location location);

    @GetMapping("/local/get/{lid}")
    public Location getLocalById(@PathVariable("lid") Integer localId);

    @GetMapping("/local/get")
    public List<Location> getLocals();
}
