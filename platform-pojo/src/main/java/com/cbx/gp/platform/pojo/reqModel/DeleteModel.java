package com.cbx.gp.platform.pojo.reqModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname DeleteModel
 * @Description TODO
 * @Date 2020/5/8 22:11
 * @Created by CBX
 */
public class DeleteModel implements Serializable {
  private List<Integer> ids;

  public List<Integer> getIds() {
    return ids;
  }

  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }
}
