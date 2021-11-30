package com.mildlamb.springcloud.mapper;

import com.mildlamb.springcloud.pojo.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("LocationDao")
public interface LocationDao {
    boolean addLocation(Location location);

    Location selectLocById(@Param("lid") Integer id);

    List<Location> selectAllLoc();
}
