package com.cbx.gp.platform.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.reqModel.Pagination;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @RequestMapping("/listCdpCollectDef")
  public ResponseModel<CdpCollectDef> listCdpCollectDef(){
    RequestModel<CdpCollectDef> requestModel=new RequestModel<>();
    requestModel.setPagination(new Pagination(1,10));
    return dataCollectService.listCollectDef(requestModel);
  }

  
}
