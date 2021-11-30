package com.mildlamb.springcloud.controller;

import com.mildlamb.springcloud.pojo.Location;
import com.mildlamb.springcloud.service.LocationClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
