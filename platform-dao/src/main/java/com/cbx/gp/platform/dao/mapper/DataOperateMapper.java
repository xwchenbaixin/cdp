package com.cbx.gp.platform.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname DataOperateMapper
 * @Description TODO
 * @Date 2020/5/10 23:30
 * @Created by CBX
 */
public interface DataOperateMapper {

  @Insert({"INSERT TABLE ${tableName}(${cols}) "
          ,"${tableData}"})
  int insertData(@Param("tableName") String tableName,
                 @Param("cols") String cols,
                 @Param("tableData") String tableData);

  @Insert("${sql}")
  int createTable(@Param("sql") String sql);
}
