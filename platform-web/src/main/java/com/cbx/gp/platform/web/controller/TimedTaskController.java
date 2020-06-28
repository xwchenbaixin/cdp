package com.cbx.gp.platform.web.controller;

import com.cbx.gp.platform.pojo.entity.CdpTimedTask;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.TimedTaskService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname TimedTaskController
 * @Description TODO
 * @Date 2020/5/31 16:01
 * @Created by CBX
 */
@RestController
@RequestMapping("/timedtask")
public class TimedTaskController {
  @Reference(retries = -1,timeout = 2000)
  private TimedTaskService timedTaskService;

  @RequestMapping("/addJod")
  public ResponseModel<CdpTimedTask> addJod(@RequestBody RequestModel<CdpTimedTask> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return timedTaskService.addJod(req);
  }

  @RequestMapping("/deleteTask")
  public ResponseModel<CdpTimedTask> deleteTask(@RequestBody RequestModel<CdpTimedTask> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return timedTaskService.deleteTask(req);
  }
  @RequestMapping("/pauseTask")
  public ResponseModel<CdpTimedTask> pauseTask(@RequestBody RequestModel<CdpTimedTask> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return timedTaskService.pauseTask(req);
  }
  @RequestMapping("/resumeTask")
  public ResponseModel<CdpTimedTask> resumeTask(@RequestBody RequestModel<CdpTimedTask> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return timedTaskService.resumeTask(req);
  }
  @RequestMapping("/listTimedTask")
  public ResponseModel<CdpTimedTask> listTimedTask(@RequestBody RequestModel<CdpTimedTask> req){

    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return timedTaskService.listTimedTask(req);
  }

  @RequestMapping("/updateTimedTask")
  public ResponseModel<CdpTimedTask> updateTimedTask(@RequestBody RequestModel<CdpTimedTask> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return timedTaskService.updateTimedTask(req);
  }

}
