package com.cbx.gp.platform.dao.quartz.model;

import java.math.BigDecimal;

/**
 * @Classname TsQuartzJob
 * @Description TODO
 * @Date 2020/4/12 20:12
 * @Created by CBX
 */
public class TsQuartzJob {
  private BigDecimal jobId;
  private String jobName;
  private String groupName;
  private String cron;
  private String param;
  private String status;
  private String createTime;

  public BigDecimal getJobId() {
    return jobId;
  }

  public void setJobId(BigDecimal jobId) {
    this.jobId = jobId;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "TsQuartzJob{" +
            "jobId=" + jobId +
            ", jobName='" + jobName + '\'' +
            ", groupName='" + groupName + '\'' +
            ", cron='" + cron + '\'' +
            ", param='" + param + '\'' +
            ", status='" + status + '\'' +
            ", createTime='" + createTime + '\'' +
            '}';
  }
}
