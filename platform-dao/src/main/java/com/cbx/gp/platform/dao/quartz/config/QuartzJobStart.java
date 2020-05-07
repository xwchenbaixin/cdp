package com.cbx.gp.platform.dao.quartz.config;

import com.cbx.gp.platform.dao.quartz.mapper.TsQuartzJobMapper;
import com.cbx.gp.platform.dao.quartz.scheduler.MyQuartzScheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzJobStart implements InitializingBean {


  @Autowired
  private MyQuartzScheduler myQuartzScheduler;

  @Autowired
  private TsQuartzJobMapper tsQuartzJobMapper;

  /**
   * 初始启动quartz
   */

  @Override
  public void afterPropertiesSet() throws Exception {
    try {
        System.out.println("任务已经启动...");
        //myQuartzScheduler.initJob(tsQuartzJobMapper.listQuartzJob());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}