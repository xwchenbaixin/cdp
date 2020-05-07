package com.cbx.gp.platform.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cbx.gp.platform.dao.mapper.CdpPermissionMapper;
import com.cbx.gp.platform.pojo.bean.MenuTree;
import com.cbx.gp.platform.pojo.entity.CdpPermission;
import com.cbx.gp.platform.service.interfaces.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname SystemServiceImpl
 * @Description TODO
 * @Date 2020/4/24 13:58
 * @Created by CBX
 */
@Service
public class SystemServiceImpl implements SystemService {
  @Autowired
  private CdpPermissionMapper cdpPermissionMapper;

  @Override
  public List<MenuTree> getMenuTree() {
    //获取根菜单
    List<CdpPermission> cdpPermissions=cdpPermissionMapper.listCdpPermission(0);

    return makeMenutree(cdpPermissions);
  }

  @Override
  public List<CdpPermission> getMenuData() {
    return null;
  }

  public List<MenuTree> makeMenutree(List<CdpPermission> children){
    List<MenuTree> menuTrees= null;
    if(children!=null) {
      menuTrees= new ArrayList<>();
      for (CdpPermission cp : children) {
        MenuTree node = new MenuTree();
        node.setNode(cp);
        List<CdpPermission> nextChildren = cdpPermissionMapper.listCdpPermission(cp.getPid());
        node.setChildren(makeMenutree(nextChildren));
        menuTrees.add(node);
      }
    }
    return menuTrees;
  }
}
