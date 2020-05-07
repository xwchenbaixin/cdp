package com.cbx.gp.platform.pojo.reqModel;

import java.io.Serializable;

/**
 * @Classname RequestModel
 * @Description TODO
 * @Date 2020/5/7 13:37
 * @Created by CBX
 */
public class RequestModel<T> implements Serializable {
  public Pagination pagination;
  public T param;

  public Pagination getPagination() {
    return pagination;
  }

  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }

  public T getParam() {
    return param;
  }

  public void setParam(T param) {
    this.param = param;
  }
}
