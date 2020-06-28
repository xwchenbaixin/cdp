package com.cbx.gp.platform.web.controller;


import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.reqModel.DeleteModel;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.security.model.User;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import com.cbx.gp.platform.service.interfaces.DataSetService;
import com.cbx.gp.platform.service.interfaces.WebmagicService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Classname DataCollectController
 * @Description TODO
 * @Date 2020/5/7 10:41
 * @Created by CBX
 */
@RestController
@RequestMapping("/dataCollect")
public class DataCollectController {

  @Reference
  private DataCollectService dataCollectService;

  @Reference
  private DataSetService dataSetService;

  @RequestMapping("/listCdpCollectDef")
  public ResponseModel<CdpCollectDef> listCdpCollectDef(@RequestBody RequestModel<CdpCollectDef> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataCollectService.listCollectDef(req);
  }


  @RequestMapping("/addCdpCollectDef")
  public ResponseModel<CdpCollectDef> addCdpCollectDef(@RequestBody RequestModel<CdpCollectDef> req) {
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataCollectService.addCdpCollectDef(req);
  }

  @RequestMapping("/getCollectDefById")
  public ResponseModel<CdpCollectDef> getCollectDefById(@RequestBody RequestModel<CdpCollectDef> req) {
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataCollectService.getCollectDefById(req);
  }

  @RequestMapping("/updateCdpCollectDef")
  public ResponseModel<CdpCollectDef> updateCdpCollectDef(@RequestBody RequestModel<CdpCollectDef> req) {
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataCollectService.updateCdpCollectDef(req);
  }

  @RequestMapping("/deleteCdpCollectDefByIds")
  public ResponseModel<CdpCollectDef> deleteCdpCollectDefByIds(@RequestBody DeleteModel dm) {
    return dataCollectService.deleteCollectDefByIds(dm);
  }


}
