package com.mildlamb.springcloud.service.impl;

import com.mildlamb.springcloud.mapper.LocationDao;
import com.mildlamb.springcloud.pojo.Location;
import com.mildlamb.springcloud.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalServiceImpl implements LocalService {

    @Autowired
    private LocationDao locationDao;

    public boolean addLocation(Location location) {
        return locationDao.addLocation(location);
    }

    public Location selectLocById(Integer id) {
        return locationDao.selectLocById(id);
    }

    public List<Location> selectAllLoc() {
        return locationDao.selectAllLoc();
    }
}
