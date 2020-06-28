package com.cbx.gp.webmagic.service.databaseManager;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

/**
 * @Classname HiveDwDatabaseManager
 * @Description TODO
 * @Date 2020/6/5 15:27
 * @Created by CBX
 */
public class HiveDwDatabaseManager {
  private static String driverName =
          "org.apache.hive.jdbc.HiveDriver";

  public static Connection getConnection(){
    Connection con=null;
    try {
      Class.forName(driverName);
      con = DriverManager.getConnection("jdbc:hive2://s3203y0292.zicp.vip:26847/cdp_dw");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return con;
  }
}
