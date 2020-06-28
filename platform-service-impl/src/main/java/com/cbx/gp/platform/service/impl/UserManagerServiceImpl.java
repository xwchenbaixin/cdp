package com.cbx.gp.platform.service.impl;

import com.cbx.gp.platform.dao.mapper.CdpUserMapper;
import com.cbx.gp.platform.dao.mapper.CdpUserRoleMapper;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.entity.CdpUserExample;
import com.cbx.gp.platform.pojo.entity.CdpUserRole;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.UserManagerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @Classname UserManagerServiceImpl
 * @Description TODO
 * @Date 2020/5/17 9:20
 * @Created by CBX
 */
@Service
public class UserManagerServiceImpl implements UserManagerService {

  @Autowired
  private CdpUserMapper cdpUserMapper;

  @Autowired
  private CdpUserRoleMapper cdpUserRoleMapper;
  @Override
  public ResponseModel<CdpUser> listUserInfo(RequestModel<CdpUser> req) {
    ResponseModel<CdpUser> res=new ResponseModel<>();

    CdpUserExample cue=new CdpUserExample();
    CdpUserExample.Criteria criteria = cue.createCriteria();

    if(req.getParam()!=null&&req.getParam().getEmail()!=null){
      criteria.andEmailLike("%"+req.getParam().getEmail()+"%");
    }
    if(req.getParam()!=null&&req.getParam().getNickname()!=null){
      criteria.andNicknameLike("%"+req.getParam().getNickname()+"%");
    }
    if(req.getParam()!=null&&req.getParam().getPhone()!=null){
      criteria.andPhoneLike("%"+req.getParam().getPhone()+"%");
    }
    if(req.getParam()!=null&&req.getParam().getUsername()!=null){
      criteria.andUsernameLike("%"+req.getParam().getUsername()+"%");
    }

    PageHelper.startPage(req.getPagination().getCurrPage(),req.getPagination().getPageSize());
    PageInfo<CdpUser> pageInfo=new PageInfo<>(cdpUserMapper.selectByExample(cue));
    res.setStatus(200);
    res.setTotal(pageInfo.getTotal());
    res.setData(pageInfo.getList());
    res.setMessage("查询成功");
    return res;
  }

  @Override
  public ResponseModel<CdpUser> banedUser(RequestModel<CdpUser> req) {
    ResponseModel<CdpUser> res=new ResponseModel<>();
    CdpUser cu=new CdpUser();
    cu.setId(req.getParam().getId());
    cu.setStatus(0);
    if(cdpUserMapper.updateByPrimaryKeySelective(cu)==1){
      res.setStatus(200);
      res.setMessage("添加成功");
    }
    return res;
  }

  @Override
  public ResponseModel<CdpUser> enabledUser(RequestModel<CdpUser> req) {
    ResponseModel<CdpUser> res=new ResponseModel<>();
    CdpUser cu=new CdpUser();
    cu.setId(req.getParam().getId());
    cu.setStatus(1);
    if(cdpUserMapper.updateByPrimaryKeySelective(cu)==1){
      res.setStatus(200);
      res.setMessage("添加成功");
    }
    return res;
  }

  @Override
  @Transactional
  public ResponseModel<CdpUser> insertUser(RequestModel<CdpUser> req) {
    ResponseModel<CdpUser> res=new ResponseModel<>();
    req.getParam().setPassword(DigestUtils.md5DigestAsHex(req.getParam().getPassword().getBytes()));


    if(cdpUserMapper.insertSelective(req.getParam())==1){
      CdpUserRole cdpUserRole=new CdpUserRole();
      cdpUserRole.setRoleId(1);
      cdpUserRole.setUserId(req.getParam().getId());
      cdpUserRole.setStatus(1);
      cdpUserRoleMapper.insertSelective(cdpUserRole);
      res.setStatus(200);
    }
    return res;
  }

  @Override
  public ResponseModel<CdpUser> getUserById(RequestModel<CdpUser> req) {
    ResponseModel<CdpUser> res=new ResponseModel<>();
    res.setData(cdpUserMapper.selectByPrimaryKey(req.getParam().getId()));
    res.setStatus(200);
    return res;
  }

  @Override
  public ResponseModel<CdpUser> upgradeUser(RequestModel<CdpUser> req) {
    ResponseModel<CdpUser> res=new ResponseModel<>();
    req.getParam().setCreateTime(null);
    req.getParam().setPassword(null);
    cdpUserMapper.updateByPrimaryKeySelective(req.getParam());
    res.setStatus(200);
    return res;
  }

  @Override
  public ResponseModel<CdpUser> resetPassword(CdpUser cu) {
    ResponseModel<CdpUser> res=new ResponseModel<>();
    cdpUserMapper.updateByPrimaryKeySelective(cu);
    res.setStatus(200);
    res.setMessage("重置成功");
    return res;
  }
}
