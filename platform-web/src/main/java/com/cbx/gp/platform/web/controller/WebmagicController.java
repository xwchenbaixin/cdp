package com.cbx.gp.platform.web.controller;

import org.apache.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import com.cbx.gp.platform.service.interfaces.WebmagicService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname WebmagicController
 * @Description TODO
 * @Date 2020/5/12 11:22
 * @Created by CBX
 */
@RestController
@RequestMapping("/webmagic")
public class WebmagicController {

  @Reference(retries = -1,timeout = 30000)
  private DataCollectService dataCollectService;

  @Reference(retries = -1,timeout = 30000)
  private WebmagicService webmagicService;

  @RequestMapping("/webmagicSyncRun")
  public ResponseModel<Map<String,String>> webmagicSyncRun(@RequestBody RequestModel<CdpCollectDef> req) {
    System.out.println("webmagicSyncRun");
    return dataCollectService.webmagicSyncRun(req);
  }


  @RequestMapping("/webmagicAsyncStart")
  public ResponseModel<Map<String,String>> webmagicAsyncStart(@RequestBody RequestModel<CdpCollectDef> req) {
    return dataCollectService.webmagicAsyncStart(req);
  }

  @RequestMapping("/getResultData")
  public ResponseModel<Map<String,String>> getResultData(@RequestBody RequestModel<CdpDataSetDef> req){
    ResponseModel<Map<String,String>> res=new ResponseModel<>();
    res.setData(webmagicService.getResultData(req.getParam().getTableName(),"LIMIT 100"));
    res.setStatus(200);
    return  res;
  }

  @RequestMapping("/dataAnalyze")
  public ResponseModel<LinkedHashMap<String,String>> dataAnalyze(String sql){
    ResponseModel<LinkedHashMap<String,String>> res=webmagicService.dataAnalyze(sql);
    return  res;
  }
}
