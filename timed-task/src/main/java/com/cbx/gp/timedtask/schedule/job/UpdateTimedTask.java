package com.cbx.gp.timedtask.schedule.job;

import com.cbx.gp.platform.dao.mapper.CdpTimedTaskMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Classname UpdateTimedTask
 * @Description TODO
 * @Date 2020/5/31 16:07
 * @Created by CBX
 */
@Component
public class UpdateTimedTask implements Job {
  @Autowired
  private CdpTimedTaskMapper cdpTimedTaskMapper;
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    cdpTimedTaskMapper.updateStatus(0);
  }
}
