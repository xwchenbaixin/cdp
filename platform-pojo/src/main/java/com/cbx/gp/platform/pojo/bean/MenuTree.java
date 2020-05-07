package com.cbx.gp.platform.pojo.bean;

import com.cbx.gp.platform.pojo.entity.CdpPermission;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname MenuTree
 * @Description TODO
 * @Date 2020/4/24 13:44
 * @Created by CBX
 */
public class MenuTree implements Serializable {
  CdpPermission node;
  List<MenuTree> children;


  public CdpPermission getNode() {
    return node;
  }

  public void setNode(CdpPermission node) {
    this.node = node;
  }

  public List<MenuTree> getChildren() {
    return children;
  }

  public void setChildren(List<MenuTree> children) {
    this.children = children;
  }
}
