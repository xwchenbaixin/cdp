package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

/**
 * @Classname DataCollectService
 * @Description TODO
 * @Date 2020/5/7 10:45
 * @Created by CBX
 */
public interface DataCollectService {

  public ResponseModel<CdpCollectDef> listCollectDef(RequestModel<CdpCollectDef> recode);
}
