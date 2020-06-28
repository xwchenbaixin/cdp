package com.cbx.gp.platform.web.controller;

import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.UserManagerService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname UserManagerController
 * @Description TODO
 * @Date 2020/5/17 9:19
 * @Created by CBX
 */
@RestController
@RequestMapping("/userManager")
public class UserManagerController {

  @Reference
  private UserManagerService userManagerService;

  @RequestMapping("/listUserInfo")
  public ResponseModel<CdpUser> listUserInfo(@RequestBody RequestModel<CdpUser> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return userManagerService.listUserInfo(req);
  }

  @RequestMapping("/banedUser")
  public ResponseModel<CdpUser> banedUser(@RequestBody RequestModel<CdpUser> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return userManagerService.banedUser(req);
  }
  @RequestMapping("/enabledUser")
  public ResponseModel<CdpUser> enabledUser(@RequestBody RequestModel<CdpUser> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return userManagerService.enabledUser(req);
  }

  @RequestMapping("/registUser")
  public ResponseModel<CdpUser> insertUser(@RequestBody RequestModel<CdpUser> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return userManagerService.insertUser(req);
  }
  @RequestMapping("/getUserById")
  public ResponseModel<CdpUser> getUserById(@RequestBody RequestModel<CdpUser> req){
    return userManagerService.getUserById(req);
  }
  @RequestMapping("/upgradeUser")
  public ResponseModel<CdpUser> upgradeUser(@RequestBody RequestModel<CdpUser> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return userManagerService.upgradeUser(req);
  }
  @RequestMapping("/resetPassword")
  public ResponseModel<CdpUser> resetPassword(String newPassword,
                                              String repeatPassword ,
                                              Integer id){
    ResponseModel<CdpUser> res=new ResponseModel<>();
    CdpUser cu=new CdpUser();
    if(!newPassword.equals(repeatPassword)){
      res.setStatus(500);
      res.setMessage("两次输入密码不一致");
      return res;
    }
    String regex="^[a-zA-Z]\\w{5,17}$";
    Pattern p=Pattern.compile(regex);
    Matcher m=p.matcher(newPassword);
    if(!m.find()){
      res.setStatus(500);
      res.setMessage("密码长度在6-18之间，以字母开头，只能包含字符、数字和下划线。");
      return res;
    }
    cu.setId(id);
    cu.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
    res=userManagerService.resetPassword(cu);
    return res;
  }

}
