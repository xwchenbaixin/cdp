package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.bean.MenuTree;
import com.cbx.gp.platform.pojo.entity.CdpPermission;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;

import java.util.List;

/**
 * @Classname SystemService
 * @Description TODO
 * @Date 2020/4/24 13:58
 * @Created by CBX
 */
public interface SystemService {
  List<MenuTree> getMenuTree(RequestModel<MenuTree> req);

  List<CdpPermission> getMenuData(RequestModel<MenuTree> req);
}
