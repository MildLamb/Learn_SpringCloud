package com.mildlamb.springcloud.service;

import com.mildlamb.springcloud.pojo.Location;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocalService {
    boolean addLocation(Location location);

    Location selectLocById(@Param("lid") Integer id);

    List<Location> selectAllLoc();
}
