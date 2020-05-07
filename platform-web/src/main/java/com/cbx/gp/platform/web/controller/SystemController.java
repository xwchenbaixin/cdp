package com.cbx.gp.platform.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.bean.MenuTree;
import com.cbx.gp.platform.pojo.entity.CdpPermission;
import com.cbx.gp.platform.service.interfaces.SystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname SystemController
 * @Description TODO
 * @Date 2020/4/24 13:42
 * @Created by CBX
 */
@RestController
@RequestMapping("/system")
public class SystemController {
  @Reference
  private SystemService systemService;

  @PostMapping("getMenuTree")
  public List<MenuTree> getMenuTree(){
    return systemService.getMenuTree();

  }

  @PostMapping("getMenuData")
  public List<CdpPermission> getMenuData(){
    return systemService.getMenuData();
  }
}
