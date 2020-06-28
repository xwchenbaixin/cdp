package com.cbx.gp.webmagic.service.core;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.webmagic.service.databaseManager.DwDatabaseManager;
import com.cbx.gp.webmagic.service.databaseManager.HiveDwDatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @Classname WebMagicUtils
 * @Description TODO
 * @Date 2020/5/3 9:54
 * @Created by CBX
 */
public class WebMagicUtils {
  static  final Logger logger = LoggerFactory.getLogger(WebMagicUtils.class);
  //static Connection dwConnection = DwDatabaseManager.getConnection();
  //static Connection dwConnection = HiveDwDatabaseManager.getConnection();


  public static String getAbsoluteURL(String baseURI, String relativePath){
    String abURL=null;
    try {
      URI base=new URI(baseURI);//基本网页URI
      URI abs=base.resolve(relativePath);//解析于上述网页的相对URL，得到绝对URI
      URL absURL=abs.toURL();//转成URL
      abURL = absURL.toString();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } finally{
      return abURL;
    }
  }

  public static String getDomain(String url){
    String domain=null;
    try {
      domain=new URI(url).getHost();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return domain;
  }

  public static void setHeader(Site site, String headers){
    if(headers==null || headers.equals("")){
      return ;
    }
    Map<String,Object> headersMap= JSON.parseObject(headers,Map.class);
    for(Map.Entry<String,Object> e:headersMap.entrySet()){
      site.addHeader(e.getKey(),String.valueOf(e.getValue()));
    }
  }


  public static List<Map<String,String>> getResult(String tableName,String limit){

    List<Map<String, String>> dataList = null;
    try {
      Connection dwConnection= HiveDwDatabaseManager.getConnection();

      dataList=new ArrayList<Map<String,String>>();
      String sql="select * from cdp_dw.`"+ tableName+"` "+limit;
      ResultSet rs=dwConnection.createStatement().executeQuery(sql);
      ResultSetMetaData md = rs.getMetaData();//获得结果集结构信息,元数据
      int columnCount = 0;   //获得列数
      columnCount = md.getColumnCount();
      while (rs.next()) {
        Map<String,String> rowData = new HashMap<String,String>();
        for (int i = 1; i <= columnCount; i++) {
          rowData.put(md.getColumnName(i), rs.getObject(i).toString());
        }
        dataList.add(rowData);
      }
      logger.info(sql);
      dwConnection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dataList;


  }

  public static ResponseModel<LinkedHashMap<String, String>> executeSql(String sql) {


    ResponseModel<LinkedHashMap<String, String>> res=new ResponseModel<>();
    List<LinkedHashMap<String, String>> dataList = null;
    try {
      Connection dwConnection= HiveDwDatabaseManager.getConnection();

      dataList=new ArrayList<LinkedHashMap<String,String>>();
      logger.info(sql);
      ResultSet rs=dwConnection.createStatement().executeQuery(sql);
      ResultSetMetaData md = rs.getMetaData();//获得结果集结构信息,元数据
      int columnCount = 0;   //获得列数
      columnCount = md.getColumnCount();
      while (rs.next()) {
        LinkedHashMap<String,String> rowData = new LinkedHashMap<String,String>();
        for (int i = 1; i <= columnCount; i++) {
          rowData.put(md.getColumnLabel(i), rs.getObject(i).toString());
        }
        dataList.add(rowData);
      }
      logger.info(sql);
      dwConnection.close();
      res.setStatus(200);
      res.setData(dataList);

    } catch (SQLException e) {
      res.setStatus(e.getErrorCode());
      res.setMessage(e.getLocalizedMessage());
      e.printStackTrace();
    }

    return res;
  }
}
