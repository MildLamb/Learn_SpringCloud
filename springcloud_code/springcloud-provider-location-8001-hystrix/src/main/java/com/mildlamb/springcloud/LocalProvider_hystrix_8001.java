package com.mildlamb.springcloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableEurekaClient  //在服务启动后自动注册到Eureka中
@EnableDiscoveryClient  //服务发现
@EnableHystrix
public class LocalProvider_hystrix_8001 {
    public static void main(String[] args) {
        SpringApplication.run(LocalProvider_hystrix_8001.class,args);
    }

    public ServletRegistrationBean registrationBean(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new HystrixMetricsStreamServlet());
        //这个路径是固定的
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        return registrationBean;
    }
}
