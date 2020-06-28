package com.cbx.gp.platform.web.controller;

import com.cbx.gp.platform.pojo.entity.CdpUser;
import org.apache.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.bean.MenuTree;
import com.cbx.gp.platform.pojo.entity.CdpPermission;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.SystemService;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
    RequestModel<MenuTree> req=new RequestModel<>();
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    if(user==null){
      return null;
    }
    return systemService.getMenuTree(req);

  }

  @PostMapping("getMenuData")
  public List<CdpPermission> getMenuData(){
    RequestModel<MenuTree> req=new RequestModel<>();
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    if(user==null){
      return null;
    }
    return systemService.getMenuData(req);
  }


}
