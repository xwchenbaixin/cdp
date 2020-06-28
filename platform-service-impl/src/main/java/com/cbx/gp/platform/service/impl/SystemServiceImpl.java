package com.cbx.gp.platform.service.impl;

//import com.alibaba.dubbo.config.annotation.Service;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import org.apache.dubbo.config.annotation.Service;
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
  public List<MenuTree> getMenuTree(RequestModel<MenuTree> req) {
    //获取根菜单
    List<CdpPermission> cdpPermissions=cdpPermissionMapper.listCdpPermission(0,req.getUser().getId());

    return makeMenutree(cdpPermissions,req);
  }

  @Override
  public List<CdpPermission> getMenuData(RequestModel<MenuTree> req) {
    return null;
  }

  public List<MenuTree> makeMenutree(List<CdpPermission> children,RequestModel<MenuTree> req){
    List<MenuTree> menuTrees= null;
    if(children!=null) {
      menuTrees= new ArrayList<>();
      for (CdpPermission cp : children) {
        MenuTree node = new MenuTree();
        node.setNode(cp);
        List<CdpPermission> nextChildren = cdpPermissionMapper.listCdpPermission(cp.getPid(),req.getUser().getId());
        node.setChildren(makeMenutree(nextChildren,req));
        menuTrees.add(node);
      }
    }
    return menuTrees;
  }
}
