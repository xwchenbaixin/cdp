package com.cbx.gp.platform.dao.quartz.service;

import com.cbx.gp.platform.dao.quartz.mapper.TsQuartzJobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname TsQuartzJobService
 * @Description TODO
 * @Date 2020/4/12 21:48
 * @Created by CBX
 */
@Service
public class TsQuartzJobService {

  @Autowired
  private TsQuartzJobMapper tsQuartzJobMapper;

  @Transactional
  public Integer reportOperate()  {
    //使用数据库行级锁进行数据查找，获取行锁以后，其他的定时器会
    System.out.println(tsQuartzJobMapper.listQuartzJob());

    return 1;
  }
}
