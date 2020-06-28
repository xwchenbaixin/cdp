package com.cbx.gp.webmagic.service.core;

import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.ColsDef;
import com.cbx.gp.webmagic.service.databaseManager.HiveDwDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname MyPipeline
 * @Description TODO
 * @Date 2020/4/27 23:37
 * @Created by CBX
 */
public class MyPipeline implements Pipeline {

  private  final Logger logger = LoggerFactory.getLogger(MyPipeline.class);

  //private Connection con= HiveDwDatabaseManager.getConnection();;

  public MyPipeline() {

  }

  @Override
  public void process(ResultItems resultItems, Task task) {

    //Map<String,String> pageData=new HashMap<>();
    //    for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
    //      //pageData.put(entry.getKey(), (String) entry.getValue());
    //      logger.info(entry.getKey() + ":\t" + entry.getValue());
    //    }
    //判断是否是数据预览，数据预览直接获取结果


    List<Map<String,String>> dataList= (List<Map<String, String>>) resultItems.getAll().get("data");

    CdpCollectDef collectDef= (CdpCollectDef) resultItems.getAll().get("collectDef");

    List<ColsDef> colList= (List<ColsDef>) resultItems.getAll().get("colList");

    String tableName= (String) resultItems.getAll().get("tableName");
    StringBuilder sql=new StringBuilder("INSERT INTO cdp_dw.`"+ tableName+"`(${cols}) values");
    List<String> trueCols=new ArrayList<>();
    List<String> dataStr=new ArrayList<>();
    for (ColsDef cd:colList) {
      trueCols.add("`"+cd.getTableTrueCol()+"`");
    }

    for(Map<String,String> data:dataList){
      sql.append("(");
      for (ColsDef cd:colList){
        dataStr.add(data.get(cd.getUserDefCol()));
        sql.append("?,");
      }
      sql.delete(sql.length()-1,sql.length());
      sql.append("),");

    }
    sql.delete(sql.length()-1,sql.length());
    int index=sql.indexOf("${cols}");
    sql.replace(index,index+"${cols}".length(),String.join(",",trueCols));

    try {
      Connection con= HiveDwDatabaseManager.getConnection();

        PreparedStatement ps = con.prepareStatement(sql.toString());
        for(int i=0;i<dataStr.size();i++){
          ps.setString((i+1),dataStr.get(i));
        }
        ps.execute();
      con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }


    //DataSet.dataList.add(pageData);
  }


}
