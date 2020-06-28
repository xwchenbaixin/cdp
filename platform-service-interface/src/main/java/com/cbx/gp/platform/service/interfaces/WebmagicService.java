package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname WebmagicService
 * @Description TODO
 * @Date 2020/5/11 21:28
 * @Created by CBX
 */
public interface WebmagicService {
  public void webmagicSyncRun(CdpCollectDef ccd, CdpDataSetDef cdsd);

  public void webmagicAsyncStart(CdpCollectDef ccd, CdpDataSetDef cdsd,String executeParam);

  List<Map<String,String>> getResultData(String tableName,String limit);

  ResponseModel<LinkedHashMap<String, String>> dataAnalyze(String sql);
}
