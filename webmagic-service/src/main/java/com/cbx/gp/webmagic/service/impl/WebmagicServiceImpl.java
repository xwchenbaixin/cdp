package com.cbx.gp.webmagic.service.impl;

import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.WebmagicService;
import com.cbx.gp.webmagic.service.core.WebMagicStart;
import com.cbx.gp.webmagic.service.core.WebMagicUtils;
import com.cbx.gp.webmagic.service.databaseManager.CdpDatabaseManager;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname WebmagicServiceImpl
 * @Description TODO
 * @Date 2020/5/11 21:33
 * @Created by CBX
 */
@Service
public class WebmagicServiceImpl implements WebmagicService {
  private  final Logger logger = LoggerFactory.getLogger(WebmagicServiceImpl.class);

  @Override
  public void webmagicSyncRun(CdpCollectDef ccd, CdpDataSetDef cdsd) {
    Spider spider= new WebMagicStart(ccd,cdsd).getSpider();
    spider.run();
  }

  @Override
  public void webmagicAsyncStart(CdpCollectDef ccd, CdpDataSetDef cdsd,String executeParam) {
    Spider spider= new WebMagicStart(ccd,cdsd).getSpider();
    new Thread(new Runnable() {
      @Override
      public void run() {
        Connection con=CdpDatabaseManager.getConnection();
        Boolean flag=true;
        int status=1;
        try {
          spider.run();
          status =2;
        }catch (Exception e){
          e.printStackTrace();
        }
        System.out.println("结束后调用更新数据");
        try {
          PreparedStatement ps=con.prepareStatement("UPDATE cdp_collect_def set status="+status+" where id=?");
          ps .setString(1,String.valueOf(ccd.getId()));
          ps.execute();
          if(executeParam.indexOf("quartz")!=-1){
            String[] params=executeParam.split(",");
            ps=con.prepareStatement("UPDATE cdp_timed_task set status=2 where id=?");
            ps.setInt(1,Integer.valueOf(params[1]));
            ps.execute();
          }

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }).start();

  }

  @Override
  public List<Map<String, String>> getResultData(String tableName,String limit) {
    return WebMagicUtils.getResult(tableName,limit);
  }

  @Override
  public ResponseModel<LinkedHashMap<String, String>> dataAnalyze(String sql) {
    return WebMagicUtils.executeSql(sql);
  }

}
