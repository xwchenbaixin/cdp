package com.cbx.gp.platform.pojo.entity;

import java.io.Serializable;

/**
 * @Classname ParamRule
 * @Description TODO
 * @Date 2020/5/2 13:36
 * @Created by CBX
 */
public class ParamRule implements Serializable {
  private int id;

  private String xpath;
  private String regex;
  //数据类型 0-str，1-img
  private Integer dataType;
  //字段名称
  private String name;

  private String alias;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getXpath() {
    return xpath;
  }

  public void setXpath(String xpath) {
    this.xpath = xpath;
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    this.dataType = dataType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public String toString() {
    return "ParamRule{" +
            "id=" + id +
            ", xpath='" + xpath + '\'' +
            ", regex='" + regex + '\'' +
            ", dataType=" + dataType +
            ", name='" + name + '\'' +
            ", alias='" + alias + '\'' +
            '}';
  }
}
