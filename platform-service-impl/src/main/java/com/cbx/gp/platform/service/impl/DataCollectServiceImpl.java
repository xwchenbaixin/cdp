package com.cbx.gp.platform.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cbx.gp.platform.dao.mapper.CdpCollectDefMapper;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpCollectDefExample;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname DataCollectServiceImpl
 * @Description TODO
 * @Date 2020/5/7 10:44
 * @Created by CBX
 */
@Service
public class DataCollectServiceImpl implements DataCollectService {

  @Autowired
  private CdpCollectDefMapper cdpCollectDefMapper;

  public ResponseModel<CdpCollectDef> listCollectDef(RequestModel<CdpCollectDef> recode){
    CdpCollectDefExample cde=new CdpCollectDefExample();
    PageHelper.startPage(recode.getPagination().getCurrPage(),recode.getPagination().getPageSize());
    PageInfo<CdpCollectDef> pageInfo=new PageInfo<>(cdpCollectDefMapper.selectByExample(cde));
    ResponseModel res=new ResponseModel();
    res.setTotal(pageInfo.getTotal());
    res.setCode(200);
    res.setData(pageInfo.getList());
    res.setMessage("成功");
    return res;
  }
}
