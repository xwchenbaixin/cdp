package com.cbx.gp.platform.pojo.bean;

import java.io.Serializable;

public class TestPojo implements Serializable {
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestPojo{" +
                "name='" + name + '\'' +
                '}';
    }
}
