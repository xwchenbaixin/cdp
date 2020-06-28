package com.cbx.gp.platform.dao.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Classname MySQLCommentGenerator
 * @Description TODO
 * @Date 2020/5/7 23:47
 * @Created by CBX
 */
public class MySQLCommentGenerator extends DefaultCommentGenerator {
  private boolean addRemarkComments = true;
  private Properties properties;

  public MySQLCommentGenerator() {
    properties = new Properties();
  }

  @Override
  public void addConfigurationProperties(Properties properties) {
    // 获取自定义的 properties
    this.properties.putAll(properties);
  }

  @Override
  public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    String author = properties.getProperty("CBX");
    String dateFormat = properties.getProperty("dateFormat", "yyyy-MM-dd");
    SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

    // 获取表注释
    String remarks = introspectedTable.getRemarks();

    topLevelClass.addJavaDocLine("/**");
    topLevelClass.addJavaDocLine(" * " + remarks);
    topLevelClass.addJavaDocLine(" * @author " + author);
    topLevelClass.addJavaDocLine(" * @date " + dateFormatter.format(new Date()));
    topLevelClass.addJavaDocLine(" */");
  }

  @Override
  public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
    // 获取列注释
    String remarks = introspectedColumn.getRemarks();
    //根据参数和备注信息判断是否添加备注信息
    if(addRemarkComments){
//            addFieldJavaDoc(field, remarks);
      //数据库中特殊字符需要转义
//      if(remarks.contains("\"")){
//        remarks = remarks.replace("\"","'");
//      }
      //给model的字段添加swagger注解
      //field.addJavaDocLine("@ApiModelProperty(value = \""+remarks+"\")");

      // createdTime或者updatedTime时间格式化
      if("createTime".equals(field.getName()) || "updateTime".equals(field.getName())) {
        field.addJavaDocLine("@JsonFormat(pattern=\"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")");
      }
    }

  }

  @Override
  public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

  }

  @Override
  public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

  }
}
