package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpTimedTask;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

public interface TimedTaskService {

  ResponseModel<CdpTimedTask> addJod(RequestModel<CdpTimedTask> req);

  ResponseModel<CdpTimedTask> deleteTask(RequestModel<CdpTimedTask> req);

  ResponseModel<CdpTimedTask> pauseTask(RequestModel<CdpTimedTask> req);

  ResponseModel<CdpTimedTask> resumeTask(RequestModel<CdpTimedTask> req);

  ResponseModel<CdpTimedTask> listTimedTask(RequestModel<CdpTimedTask> req);

  ResponseModel<CdpTimedTask> updateTimedTask(RequestModel<CdpTimedTask> req);
}
