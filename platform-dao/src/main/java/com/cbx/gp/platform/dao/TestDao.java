package com.cbx.gp.platform.dao;

import com.cbx.gp.platform.pojo.TestPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestDao {
    @Select("select name from user;")
    public TestPojo getName();
}
