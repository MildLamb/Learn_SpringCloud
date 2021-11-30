package com.mildlamb.springcloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

// Location实体类
// orm类表关系映射
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)  //开启链式写法
public class Location implements Serializable {
    private Integer localId;
    private String localName;

    //表明这个数据是存在哪个数据库的  微服务，可以一个服务对应一个数据库，同一个信息可能是不同数据库的
    //所以用这个字段来区分 数据是来自哪个数据库
    private String dbSource;

    public Location(String localName){
        this.localName = localName;
    }
}
