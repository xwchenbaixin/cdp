package com.cbx.gp.timedtask.schedule.impl;

import com.cbx.gp.platform.dao.mapper.CdpCollectDefMapper;
import com.cbx.gp.platform.dao.mapper.CdpDataSetDefMapper;
import com.cbx.gp.platform.dao.mapper.CdpTimedTaskMapper;
import com.cbx.gp.platform.pojo.entity.*;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.TimedTaskService;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import com.cbx.gp.timedtask.schedule.job.CollectJob;
import com.cbx.gp.timedtask.schedule.job.DataAnalyzeJob;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * @Classname TimedTaskService
 * @Description TODO
 * @Date 2020/5/31 11:05
 * @Created by CBX
 */
@Service
public class TimedTaskServiceImpl implements TimedTaskService {
  @Autowired
  private Scheduler scheduler;

  @Autowired
  private CdpTimedTaskMapper cdpTimedTaskMapper;

  @Autowired
  private CdpCollectDefMapper cdpCollectDefMapper;

  @Autowired
  private CdpDataSetDefMapper cdpDataSetDefMapper;

  @Override
  public ResponseModel<CdpTimedTask> addJod(RequestModel<CdpTimedTask> req){

    CdpTimedTask cdpTimedTask=req.getParam();
    ResponseModel<CdpTimedTask> res=new ResponseModel<>();

    //判断Cron是否有效
    Date nextTriggerTime=null;
    if(CronExpression.isValidExpression(cdpTimedTask.getCron())){
      //获取下一次执行时间
      try {
        nextTriggerTime=new CronExpression(cdpTimedTask.getCron()).getNextValidTimeAfter(new Date());
        cdpTimedTask.setNextTime(nextTriggerTime);
      } catch (ParseException e) {
        e.printStackTrace();
        res.setStatus(500);
        res.setMessage(e.getMessage());
        return res;
      }
    }else{
      res.setStatus(500);
      res.setMessage("Cron表达式无效，请重新设置");
      return res;
    }

    String serialNum= UUID.randomUUID().toString();
    cdpTimedTask.setSerialNum(serialNum);

    try {
      JobDetail jobDetail=null;
      if(cdpTimedTask.getTaskType()==0) {
        CdpCollectDef collectDef=cdpCollectDefMapper.selectByPrimaryKey(Integer.valueOf(cdpTimedTask.getParam()));
        CdpDataSetDef cdpDataSetDef=cdpDataSetDefMapper.selectByPrimaryKey(collectDef.getDataSetDefId());
        jobDetail = JobBuilder.newJob(CollectJob.class)
                .withIdentity(serialNum, serialNum).build();
        //定时任务参数
        jobDetail.getJobDataMap().put("ccd",collectDef);
        jobDetail.getJobDataMap().put("cdsd",cdpDataSetDef);
      }
      if(cdpTimedTask.getTaskType()==1){
        jobDetail = JobBuilder.newJob(DataAnalyzeJob.class)
                .withIdentity(serialNum, serialNum).build();

        jobDetail.getJobDataMap().put("param",cdpTimedTask);
      }

      //生成触发器
      CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cdpTimedTask.getCron());
      CronTrigger cronTrigger = TriggerBuilder.newTrigger()
              .withIdentity(serialNum, serialNum)
              //暂停后，不会触发之前的任务，以当前cron表达式开始触发执行
              .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionDoNothing())
              .withSchedule(cronScheduleBuilder).build();

      scheduler.scheduleJob(jobDetail, cronTrigger);
      scheduler.start();
      //更新定时任务信息
      cdpTimedTask.setUserId(req.getUser().getId());
      cdpTimedTaskMapper.insertSelective(cdpTimedTask);
      res.setStatus(200);

    } catch (SchedulerException e) {
      res.setStatus(500);
      res.setMessage(e.getMessage());
      e.printStackTrace();
    }
    return res;
  }

  @Override
  public ResponseModel<CdpTimedTask> pauseTask(RequestModel<CdpTimedTask> req){
    CdpTimedTask cdpTimedTask=req.getParam();
    ResponseModel<CdpTimedTask> res=new ResponseModel<>();
    String serialNum=cdpTimedTask.getSerialNum();
    JobKey jobKey = JobKey.jobKey(serialNum, serialNum);
    TriggerKey triggerKey = TriggerKey.triggerKey(serialNum, serialNum);
    try {
      scheduler.pauseTrigger(triggerKey);
      scheduler.pauseJob(jobKey);
      CdpTimedTask updateCtd=new CdpTimedTask();
      updateCtd.setId(cdpTimedTask.getId());
      updateCtd.setStatus(3);
      cdpTimedTaskMapper.updateByPrimaryKeySelective(updateCtd);
      res.setStatus(200);
    } catch (SchedulerException e) {
      res.setStatus(500);
      res.setMessage(e.getMessage());
      e.printStackTrace();
    }
    return res;

  }

  @Override
  public ResponseModel<CdpTimedTask> deleteTask(RequestModel<CdpTimedTask> req){
    CdpTimedTask cdpTimedTask=req.getParam();
    ResponseModel<CdpTimedTask> res=new ResponseModel<>();
    String serialNum=cdpTimedTask.getSerialNum();
    TriggerKey triggerKey = TriggerKey.triggerKey(serialNum, serialNum);
    try {
      scheduler.pauseTrigger(triggerKey);
      //移除任务
      if(scheduler.unscheduleJob(triggerKey)){
        cdpTimedTaskMapper.deleteByPrimaryKey(req.getParam().getId());
        res.setStatus(200);
      }else {
        res.setStatus(500);
        res.setMessage("删除失败");
      }

    } catch (SchedulerException e) {
      res.setStatus(500);
      res.setMessage(e.getMessage());
      e.printStackTrace();
    }
    return res;
  }

  @Override
  public ResponseModel<CdpTimedTask> resumeTask(RequestModel<CdpTimedTask> req){
    CdpTimedTask cdpTimedTask=req.getParam();
    ResponseModel<CdpTimedTask> res=new ResponseModel<>();

    String serialNum=cdpTimedTask.getSerialNum();
    JobKey jobKey = JobKey.jobKey(serialNum, serialNum);
    TriggerKey triggerKey = TriggerKey.triggerKey(serialNum, serialNum);
    try {
      scheduler.resumeTrigger(triggerKey);
      scheduler.resumeJob(jobKey);
      CdpTimedTask updateCtd=new CdpTimedTask();
      updateCtd.setId(cdpTimedTask.getId());
      updateCtd.setStatus(0);
      cdpTimedTaskMapper.updateByPrimaryKeySelective(updateCtd);
      res.setStatus(200);
    } catch (SchedulerException e) {
      res.setStatus(500);
      res.setMessage(e.getMessage());
      e.printStackTrace();
    }
    return res;
  }

  @Override
  public ResponseModel<CdpTimedTask> listTimedTask(RequestModel<CdpTimedTask> req){
    ResponseModel<CdpTimedTask> res=new ResponseModel<>();
    CdpTimedTaskExample ctte=new CdpTimedTaskExample();
    CdpTimedTaskExample.Criteria criteria = ctte.createCriteria();
    criteria.andUserIdEqualTo(req.getUser().getId());
    if(req.getParam()!=null&&req.getParam().getTaskName()!=null){
      criteria.andTaskNameLike("%"+req.getParam().getTaskName()+"%");
    }
    if(req.getPagination()!=null) {
      PageHelper.startPage(req.getPagination().getCurrPage(), req.getPagination().getPageSize());
    }
    PageInfo<CdpTimedTask> pageInfo=new PageInfo<>(cdpTimedTaskMapper.selectByExample(ctte));
    res.setStatus(200);
    res.setTotal(pageInfo.getTotal());
    res.setData(pageInfo.getList());
    res.setMessage("查询成功");

    return res;
  }

  @Override
  public ResponseModel<CdpTimedTask> updateTimedTask(RequestModel<CdpTimedTask> req) {
    ResponseModel<CdpTimedTask> res=new ResponseModel<>();
    CdpTimedTask cdpTimedTask=req.getParam();
    //判断Cron是否有效
    Date nextTriggerTime=null;
    if(CronExpression.isValidExpression(cdpTimedTask.getCron())) {
      //获取下一次执行时间
      try {
        nextTriggerTime = new CronExpression(cdpTimedTask.getCron()).getNextValidTimeAfter(new Date());
      } catch (ParseException e) {
        e.printStackTrace();
        res.setStatus(500);
        res.setMessage(e.getMessage());
        return res;
      }
    }
    try {
      String serialNum=cdpTimedTask.getSerialNum();
      TriggerKey triggerKey=new TriggerKey(serialNum,serialNum);
      Trigger trigger=scheduler.getTrigger(triggerKey);
      if(trigger!=null){
        CronTrigger oldCronTrigger= (CronTrigger) trigger;
        String oldCron=oldCronTrigger.getExpressionSummary();
        if(!oldCron.equals(cdpTimedTask.getCron())){
          CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cdpTimedTask.getCron());
          CronTrigger newCronTrigger = TriggerBuilder.newTrigger()
                  .withIdentity(serialNum, serialNum)
                  //暂停后，不会触发之前的任务，以当前cron表达式开始触发执行
                  .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionDoNothing())
                  .withSchedule(cronScheduleBuilder).build();
          scheduler.rescheduleJob(triggerKey,newCronTrigger);
          CdpTimedTask updateTimeTask=new CdpTimedTask();
          updateTimeTask.setId(cdpTimedTask.getId());
          updateTimeTask.setCron(cdpTimedTask.getCron());
          updateTimeTask.setNextTime(nextTriggerTime);
          cdpTimedTaskMapper.updateByPrimaryKeySelective(updateTimeTask);
          res.setStatus(200);
          res.setMessage("修改成功");
        }
      }else{
        res.setStatus(500);
        res.setMessage("触发器不存在");
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
      res.setStatus(500);
      res.setMessage(e.getMessage());
    }

    return res;
  }
}
