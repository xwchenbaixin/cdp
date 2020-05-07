package com.cbx.gp.platform.dao.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

//将quatz的容器交由Spring托管
//在Spring中 创建该调度器工厂
@Component
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
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