package com.mildlamb.springcloud.service;

import com.mildlamb.springcloud.pojo.Location;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

//降级
@Component
public class LocationClientServiceFallbackFactory implements FallbackFactory {
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
