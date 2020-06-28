package com.cbx.gp.webmagic.service.databaseManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Classname DwDatabaseManager
 * @Description TODO
 * @Date 2020/5/11 11:35
 * @Created by CBX
 */
public class CdpDatabaseManager {
  private static ComboPooledDataSource dataSource;
  static {
    try {
      //ComboPooledDataSource cpds = new ComboPooledDataSource（）;
      // cpds.setDriverClass（“org.postgresql.Driver”）;
      // 加载jdbc驱动程序cpds.setJdbcUrl（“jdbc：postgresql：localhost / testdb”）;
      // cpds.setUser（ “swaldman”）;
      // cpds.setPassword（ “测试密码”）;
      // 下面的设置是可选的 -  c3p0可以使用默认值
      // cpds.setMinPoolSize（5）;
      // cpds.setAcquireIncrement（5）;
      // cpds.setMaxPoolSize（20）; // DataSource cpds现在是一个完全配置和可用的汇集数据源...
      dataSource = new ComboPooledDataSource();
      dataSource.setUser("root");
      dataSource.setPassword("cbx318");
      dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/cdp?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
      dataSource.setDriverClass("com.mysql.jdbc.Driver");
      dataSource.setAcquireIncrement(5);
      dataSource.setInitialPoolSize(5); //初始化连接数
      dataSource.setMinPoolSize(1);//最小连接数
      dataSource.setMaxPoolSize(20);//最大连接数
      dataSource.setMaxStatements(50);//最长等待时间
      dataSource.setMaxIdleTime(60);//最大空闲时间，单位毫秒
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }
  }


  public static synchronized Connection getConnection() {
    Connection conn = null;
    try {
      conn=dataSource.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }
}
