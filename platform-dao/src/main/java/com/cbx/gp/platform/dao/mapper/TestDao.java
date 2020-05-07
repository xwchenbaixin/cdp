package com.cbx.gp.platform.dao.mapper;

import com.cbx.gp.platform.pojo.bean.TestPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestDao {
    @Select("select name from user;")
    public TestPojo getName();
}
