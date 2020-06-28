package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.reqModel.DeleteModel;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

import java.util.List;
import java.util.Map;

/**
 * @Classname DataCollectService
 * @Description TODO
 * @Date 2020/5/7 10:45
 * @Created by CBX
 */
public interface DataCollectService {

  ResponseModel<CdpCollectDef> listCollectDef(RequestModel<CdpCollectDef> recode);

  ResponseModel<CdpCollectDef> addCdpCollectDef(RequestModel<CdpCollectDef> req);

  ResponseModel<CdpCollectDef> updateCdpCollectDef(RequestModel<CdpCollectDef> req);

  ResponseModel<CdpCollectDef> getCollectDefById(RequestModel<CdpCollectDef> req);

  ResponseModel<CdpCollectDef> deleteCollectDefByIds(DeleteModel dm);

  public ResponseModel<Map<String,String>> webmagicSyncRun(RequestModel<CdpCollectDef> req);


  public ResponseModel<Map<String,String>> webmagicAsyncStart(RequestModel<CdpCollectDef> req);
}
