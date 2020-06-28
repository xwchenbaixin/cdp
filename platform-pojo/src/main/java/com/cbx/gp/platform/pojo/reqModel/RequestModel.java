package com.cbx.gp.platform.pojo.reqModel;

import com.cbx.gp.platform.pojo.entity.CdpUser;

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
  public CdpUser user;
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

  public CdpUser getUser() {
    return user;
  }

  public void setUser(CdpUser user) {
    this.user = user;
  }
}
