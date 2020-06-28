package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

/**
 * @Classname UserManagerService
 * @Description TODO
 * @Date 2020/5/17 9:20
 * @Created by CBX
 */
public interface UserManagerService {

  ResponseModel<CdpUser> listUserInfo(RequestModel<CdpUser> req);

  ResponseModel<CdpUser> banedUser(RequestModel<CdpUser> req);

  ResponseModel<CdpUser> enabledUser(RequestModel<CdpUser> req);

  ResponseModel<CdpUser> insertUser(RequestModel<CdpUser> req);

  ResponseModel<CdpUser> getUserById(RequestModel<CdpUser> req);

  ResponseModel<CdpUser> upgradeUser(RequestModel<CdpUser> req);

  ResponseModel<CdpUser> resetPassword(CdpUser cu);

}
