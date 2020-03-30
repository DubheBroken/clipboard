package com.rice.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class BaseModel implements Serializable {

    public String toJson() {
        return JSON.toJSONString(this);
    }


}
