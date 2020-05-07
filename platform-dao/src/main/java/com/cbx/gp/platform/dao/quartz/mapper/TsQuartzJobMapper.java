package com.cbx.gp.platform.dao.quartz.mapper;

import com.cbx.gp.platform.dao.quartz.model.TsQuartzJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Classname TsQuartzJobMapper
 * @Description TODO
 * @Date 2020/4/12 20:09
 * @Created by CBX
 */
@Mapper
public interface TsQuartzJobMapper {

  @Select("SELECT * FROM ts_quartz_job where job_id=1 for update skip locked")
  List<TsQuartzJob> listQuartzJob();

  @Update("update ts_quartz_job set status=2 where job_id=1")
  Integer updateQuartzJob();
}
