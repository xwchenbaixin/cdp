package com.cbx.gp.platform.dao.quartz.scheduler;

import com.cbx.gp.platform.dao.quartz.job.ReportOperateQuartzJob;
import com.cbx.gp.platform.dao.quartz.model.TsQuartzJob;
import org.quartz.*;

import java.util.Date;
import java.util.List;


/**
 * 任务调度处理
 * @author yvan
 *
 */

public class MyQuartzScheduler {
  // 任务调度
  private Scheduler scheduler;

  /**
   * 开始执行所有任务
   *
   * @throws SchedulerException
   */
  public void startJob() throws SchedulerException {
    scheduler.start();
  }

  public void setScheduler(Scheduler scheduler) {
    this.scheduler=scheduler;
  }
  /**
   * 获取Job信息
   *
   * @param name
   * @param group
   * @return
   * @throws SchedulerException
   */
  public String getJobInfo(String name, String group) throws SchedulerException {
    TriggerKey triggerKey = new TriggerKey(name, group);
    CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
            scheduler.getTriggerState(triggerKey).name());
  }

  /**
   * 修改某个任务的执行时间
   *
   * @param name
   * @param group
   * @param time
   * @return
   * @throws SchedulerException
   */
  public boolean modifyJob(String name, String group, String time) throws SchedulerException {
    Date date = null;
    TriggerKey triggerKey = new TriggerKey(name, group);
    CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    String oldTime = cronTrigger.getCronExpression();
    if (!oldTime.equalsIgnoreCase(time)) {
      CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
      CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
              .withSchedule(cronScheduleBuilder).build();
      date = scheduler.rescheduleJob(triggerKey, trigger);
    }
    return date != null;
  }

  /**
   * 暂停所有任务
   *
   * @throws SchedulerException
   */
  public void pauseAllJob() throws SchedulerException {
    scheduler.pauseAll();
  }

  /**
   * 暂停某个任务
   *
   * @param name
   * @param group
   * @throws SchedulerException
   */
  public void pauseJob(String name, String group) throws SchedulerException {
    JobKey jobKey = new JobKey(name, group);
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    if (jobDetail == null)
      return;
    scheduler.pauseJob(jobKey);
  }

  /**
   * 恢复所有任务
   *
   * @throws SchedulerException
   */
  public void resumeAllJob() throws SchedulerException {
    scheduler.resumeAll();
  }

  /**
   * 恢复某个任务
   *
   * @param name
   * @param group
   * @throws SchedulerException
   */
  public void resumeJob(String name, String group) throws SchedulerException {
    JobKey jobKey = new JobKey(name, group);
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    if (jobDetail == null)
      return;
    scheduler.resumeJob(jobKey);
  }

  /**
   * 删除某个任务
   *
   * @param name
   * @param group
   * @throws SchedulerException
   */
  public void deleteJob(String name, String group) throws SchedulerException {
    JobKey jobKey = new JobKey(name, group);
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    if (jobDetail == null)
      return;
    scheduler.deleteJob(jobKey);
  }

  public void addJob(TsQuartzJob tsQuartzJob, Class T) throws SchedulerException {
    // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
    // JobDetail 是具体Job实例
    JobDetail jobDetail = JobBuilder.newJob(T).withIdentity(tsQuartzJob.getJobName(), tsQuartzJob.getGroupName()).build();
    // 基于表达式构建触发器
    CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(tsQuartzJob.getCron());
    // CronTrigger表达式触发器 继承于Trigger
    // TriggerBuilder 用于构建触发器实例
    CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(tsQuartzJob.getJobName(), tsQuartzJob.getGroupName())
            .withSchedule(cronScheduleBuilder).build();
    scheduler.scheduleJob(jobDetail, cronTrigger);
    scheduler.start();
  }


  //初始化数据库中的所有job
  //使用 job_name 和 group_name 区分唯一的任务
  public void initJob(List<TsQuartzJob> tsQuartzJobs) throws SchedulerException{

    for(TsQuartzJob job : tsQuartzJobs) {
      // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
      // JobDetail 是具体Job实例,初始化
      JobDetail jobDetail = JobBuilder.newJob(ReportOperateQuartzJob.class).withIdentity(job.getJobName(), job.getGroupName()).build();
      //给job传递参数，参数使用json格式储存了，所有需要定时生成的报表。
      jobDetail.getJobDataMap().put("param",job.getJobId());
      // 基于表达式构建触发器
      CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
      // CronTrigger表达式触发器 继承于Trigger
      // TriggerBuilder 用于构建触发器实例
      CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getGroupName())
              .withSchedule(cronScheduleBuilder).build();
      scheduler.scheduleJob(jobDetail, cronTrigger);
    }
    scheduler.start();
  }




  //如果需要初始化其他的JOB，则按照上面的格式进行添加代码即可
}
