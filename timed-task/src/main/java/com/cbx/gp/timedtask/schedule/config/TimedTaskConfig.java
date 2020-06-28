package com.cbx.gp.timedtask.schedule.config;
import com.alibaba.druid.pool.DruidDataSource;
import com.cbx.gp.timedtask.schedule.job.UpdateTimedTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;


/**
 * @Classname TimedTaskConfig
 * @Description TODO
 * @Date 2020/5/30 11:42
 * @Created by CBX
 */
@Configuration
public class TimedTaskConfig {

  @Autowired
  private AutowiredQuartzBeanFactory myJobFactory;

  //配置数据源
  //这里可以不使用@Bean交给spring管理,否则可能出现默认数据源的问题.
  //@Bean
  public DataSource druidDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl("jdbc:mysql://localhost:3306/cdp_timed_task?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
    dataSource.setUsername("root");
    dataSource.setPassword("cbx318");
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    return dataSource;
  }

  //配置quartz配置文件
  @Bean
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/spring-quartz.properties"));
    // 在quartz.properties中的属性被读取并注入后再初始化对象
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  //配置任务调度工厂,用来生成任务调度器,这是quartz的核心
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    //开启更新job
    factory.setOverwriteExistingJobs(true);
    //如果不配置就会使用quartz.properties中的instanceName
    //factory.setSchedulerName("Cluster_Scheduler");
    //配置数据源,这是quartz使用的表的数据库存放位置
    factory.setDataSource(druidDataSource());
    //设置实例在spring容器中的key
    factory.setApplicationContextSchedulerContextKey("applicationContext");
    //配置线程池
    factory.setTaskExecutor(schedulerThreadPool());
    //配置配置文件
    factory.setQuartzProperties(quartzProperties());

    //将自定义的MyJobFactory注入配置类,并添加如下配置,
    //配置使用spring的autowired的对象,在job中进行对象的注入
    factory.setJobFactory(myJobFactory);
    //设置延时启动,保证job中的属性的注入
    factory.setStartupDelay(5);

    return factory;
  }

  //开启当前的任务调度器
  @Bean
  public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    JobKey jobKey=JobKey.jobKey("sysUpdateTimedTask","sysUpdateTimedTask");
    if(scheduler.getJobDetail(jobKey)==null) {
      JobDetail jobDetail = JobBuilder.newJob(UpdateTimedTask.class)
              .withIdentity("sysUpdateTimedTask", "sysUpdateTimedTask").build();
      CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 */1 * ?");
      CronTrigger cronTrigger = TriggerBuilder.newTrigger()
              .withIdentity("sysUpdateTimedTask", "sysUpdateTimedTask")
              .withSchedule(cronScheduleBuilder).build();
      scheduler.scheduleJob(jobDetail, cronTrigger);
      scheduler.start();
    }
    return scheduler;
  }

  //线程池配置
  @Bean
  public Executor schedulerThreadPool() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(50);
    return executor;
  }


}
