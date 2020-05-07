package com.cbx.gp.platform.dao.quartz.job;

import com.cbx.gp.platform.dao.quartz.service.TsQuartzJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @Classname ReportOperateQuartzJob
 * @Description TODO
 * @Date 2020/4/12 20:16
 * @Created by CBX
 */

public class ReportOperateQuartzJob implements Job {

  @Autowired
  private TsQuartzJobService tsQuartzJobService;

  @Override
  public void execute(JobExecutionContext context) {

    try {
      tsQuartzJobService.reportOperate();
    } catch (RuntimeException e) {
      e.printStackTrace();
    }

  }
}
