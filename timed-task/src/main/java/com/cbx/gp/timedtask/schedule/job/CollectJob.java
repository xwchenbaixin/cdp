package com.cbx.gp.timedtask.schedule.job;

import com.cbx.gp.platform.dao.mapper.CdpTimedTaskMapper;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.entity.CdpTimedTask;
import com.cbx.gp.platform.pojo.entity.CdpTimedTaskExample;
import com.cbx.gp.platform.service.interfaces.WebmagicService;
import org.apache.dubbo.config.annotation.Reference;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;

/**
 * @Classname CollectJob
 * @Description TODO
 * @Date 2020/5/31 13:09
 * @Created by CBX
 */

public class CollectJob  implements Job {

  @Autowired
  private CdpTimedTaskMapper cdpTimedTaskMapper;

  @Reference(retries = -1,timeout = 30000)
  private WebmagicService webmagicService;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    CdpCollectDef ccd= (CdpCollectDef) context.getJobDetail().getJobDataMap().get("ccd");
    CdpDataSetDef cdsd= (CdpDataSetDef) context.getJobDetail().getJobDataMap().get("cdsd");

    //更新任务状态
    Trigger trigger=context.getTrigger();
    String serialNum=trigger.getKey().getName();
    CdpTimedTaskExample cdpTimedTaskExample=new CdpTimedTaskExample();
    cdpTimedTaskExample.createCriteria().andSerialNumEqualTo(serialNum);
    CdpTimedTask cdpTimedTask=cdpTimedTaskMapper.selectByExample(cdpTimedTaskExample).get(0);
    Date nextTriggerTime=null;

    //获取下一次执行时间
    try {
      nextTriggerTime=new CronExpression(cdpTimedTask.getCron()).getNextValidTimeAfter(new Date());
      cdpTimedTask.setLastTime(cdpTimedTask.getNextTime());
      cdpTimedTask.setNextTime(nextTriggerTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    cdpTimedTask.setStatus(1);
    cdpTimedTaskMapper.updateByPrimaryKeySelective(cdpTimedTask);

    if(ccd!=null&&cdsd!=null){
      //异步调用采集任务
      webmagicService.webmagicAsyncStart(ccd,cdsd,"quartz,"+cdpTimedTask.getId());
    }
  }
}
