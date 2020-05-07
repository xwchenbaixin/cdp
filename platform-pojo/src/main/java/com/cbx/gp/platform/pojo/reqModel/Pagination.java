package com.cbx.gp.platform.pojo.reqModel;

import java.io.Serializable;

/**
 * @Classname Pagination
 * @Description TODO
 * @Date 2020/5/7 13:43
 * @Created by CBX
 */
public class Pagination  implements Serializable {
  public int currPage;
  public int pageSize;

  public Pagination(int currPage, int pageSize) {
    this.currPage = currPage;
    this.pageSize = pageSize;
  }

  public int getCurrPage() {
    return currPage;
  }

  public void setCurrPage(int currPage) {
    this.currPage = currPage;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
