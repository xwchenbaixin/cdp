package com.cbx.gp.timedtask.schedule.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * @Classname AutowiredQuartzBeanFactory
 * @Description TODO
 * @Date 2020/5/30 11:53
 * @Created by CBX
 */
@Component
public class AutowiredQuartzBeanFactory extends SpringBeanJobFactory implements
        ApplicationContextAware {

  private transient AutowireCapableBeanFactory autowireCapablebeanFactory;

  @Override
  public void setApplicationContext(final ApplicationContext context) {
    autowireCapablebeanFactory = context.getAutowireCapableBeanFactory();
  }

  @Override
  protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
    final Object job = super.createJobInstance(bundle);
    autowireCapablebeanFactory.autowireBean(job);
    return job;
  }
}

