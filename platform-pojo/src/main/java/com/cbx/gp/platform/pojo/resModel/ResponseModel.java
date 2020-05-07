package com.cbx.gp.platform.pojo.resModel;


import java.io.Serializable;
import java.util.List;

/**
 * @Classname ResponseModel
 * @Description TODO
 * @Date 2020/5/7 11:11
 * @Created by CBX
 */
public class ResponseModel<T> implements Serializable {
  public int code;

  public Long total;
  public String message;
  public List<T> data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total = total;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }


  public ResponseModel(int code,String message,Long total,  List<T> data) {
    this.code = code;
    this.message=message;
    this.total=total;
    this.data = data;
  }

  public ResponseModel() {
  }

}
