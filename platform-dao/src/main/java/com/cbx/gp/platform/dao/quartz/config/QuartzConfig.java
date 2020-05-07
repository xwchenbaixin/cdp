package com.cbx.gp.platform.dao.quartz.config;

import com.cbx.gp.platform.dao.quartz.scheduler.MyQuartzScheduler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Classname QuartzConfig
 * @Description TODO
 * @Date 2020/4/13 16:00
 * @Created by CBX
 */
@Configuration
public class QuartzConfig {
  @Autowired
  private AutowiringSpringBeanJobFactory myJobFactory;


  /**
   * 初始注入scheduler
   * @return
   * @throws SchedulerException
   */

  //创建调度器工厂
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() throws Exception {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setOverwriteExistingJobs(true);
    //factory.setStartupDelay(20);          //延时启动，应用启动成功后在启动
    factory.setJobFactory(myJobFactory);
    return factory;
  }

  @Bean
  public MyQuartzScheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    MyQuartzScheduler myQuartzScheduler = new MyQuartzScheduler();
    myQuartzScheduler.setScheduler(scheduler);
    return myQuartzScheduler;
  }
}
