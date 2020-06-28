package com.cbx.gp.platform.pojo.resModel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ResponseModel
 * @Description TODO
 * @Date 2020/5/7 11:11
 * @Created by CBX
 */
public class ResponseModel<T> implements Serializable {
  public int status;

  public Long total;
  public String message;
  public List<T> data;
  public String error;

  public String trace;

  public String token;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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

  public void setData(T t) {
    if(this.data==null)
      this.data=new ArrayList<>();
    this.data.add(t);
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getTrace() {
    return trace;
  }

  public void setTrace(String trace) {
    this.trace = trace;
  }

  public ResponseModel(int status, String message, Long total, List<T> data) {
    this.status = status;
    this.message=message;
    this.total=total;
    this.data = data;
  }

  public ResponseModel() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
