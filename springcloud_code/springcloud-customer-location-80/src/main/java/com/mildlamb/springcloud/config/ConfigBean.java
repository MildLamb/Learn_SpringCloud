package com.mildlamb.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@LoadBalancerClient(name = "SPRINGCLOUD-PROVIDER-LOCATION",configuration = CustomLoadBalancerConfiguration.class)
public class ConfigBean {

    @Bean
    //配置负载均衡实现RestTemplate
    @LoadBalanced  //ribbon
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
