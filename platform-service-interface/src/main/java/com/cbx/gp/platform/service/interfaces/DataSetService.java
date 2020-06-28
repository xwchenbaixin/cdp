package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

/**
 * @Classname DataSetService
 * @Description TODO
 * @Date 2020/5/11 13:42
 * @Created by CBX
 */
public interface DataSetService {

  ResponseModel<CdpDataSetDef> selectByPrimaryKey(Integer id);

  ResponseModel<CdpDataSetDef> listDataSet(RequestModel<CdpDataSetDef> req);

  ResponseModel<CdpDataSetDef> saveScript(RequestModel<CdpDataSetDef> req);
}
