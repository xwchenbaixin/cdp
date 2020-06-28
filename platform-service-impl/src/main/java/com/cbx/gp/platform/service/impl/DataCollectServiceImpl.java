package com.cbx.gp.platform.service.impl;

//import com.alibaba.dubbo.config.annotation.Reference;
//import com.alibaba.dubbo.config.annotation.Service;
import org.apache.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.dao.mapper.CdpCollectDefMapper;
import com.cbx.gp.platform.dao.mapper.CdpDataSetDefMapper;
import com.cbx.gp.platform.dao.mapper.DataOperateMapper;
import com.cbx.gp.platform.pojo.entity.*;
import com.cbx.gp.platform.pojo.reqModel.DeleteModel;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import com.cbx.gp.platform.service.interfaces.WebmagicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
//import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Classname DataCollectServiceImpl
 * @Description TODO
 * @Date 2020/5/7 10:44
 * @Created by CBX
 */
@Service
@Transactional
public class DataCollectServiceImpl implements DataCollectService {

  @Autowired
  private CdpCollectDefMapper cdpCollectDefMapper;

  @Autowired
  private DataOperateMapper dataOperateMapper;

  @Autowired
  private CdpDataSetDefMapper cdpDataSetDefMapper;

  @Reference(retries = 0,timeout = 30000)
  private WebmagicService webmagicService;

  public ResponseModel<CdpCollectDef> listCollectDef(RequestModel<CdpCollectDef> recode){
    CdpCollectDefExample cde=new CdpCollectDefExample();
    cde.setOrderByClause("operate_time DESC");
    CdpCollectDefExample.Criteria criteria = cde.createCriteria();
    criteria.andUserIdEqualTo(recode.getUser().getId());

    System.out.println(recode.getUser().getId());

    if(recode.getPagination()!=null) {
      PageHelper.startPage(recode.getPagination().getCurrPage(), recode.getPagination().getPageSize());
    }
    PageInfo<CdpCollectDef> pageInfo=new PageInfo<>(cdpCollectDefMapper.selectByExample(cde));

    ResponseModel res=new ResponseModel();
    res.setTotal(pageInfo.getTotal());
    res.setStatus(200);
    res.setData(pageInfo.getList());
    res.setMessage("成功");
    return res;
  }

  @Override
  @Transactional
  public ResponseModel<CdpCollectDef> addCdpCollectDef(RequestModel<CdpCollectDef> req) {
    StringBuilder sb=new StringBuilder();
    Random random = new Random();
    for( int i = 0; i < 4; i ++) {
      int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写还是小写
      sb.append((char)(choice + random.nextInt(26)));
    }

    ResponseModel res=new ResponseModel();
    res.setStatus(200);


    CdpDataSetDef cdsd=new CdpDataSetDef();
    String tableName= req.getUser().getUsername()+sb.toString();
    CdpDataSetDefExample cdpDataSetDefExample=new CdpDataSetDefExample();
    cdpDataSetDefExample.createCriteria().andTableNameEqualTo(tableName);

    while(cdpDataSetDefMapper.selectByExample(cdpDataSetDefExample).size()>0){
      sb=new StringBuilder();
      for( int i = 0; i < 4; i ++) {
        int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写还是小写
        sb.append((char)(choice + random.nextInt(26)));
      }
      tableName= req.getUser().getUsername()+sb.toString();
      cdpDataSetDefExample.createCriteria().andTableNameEqualTo(tableName);
    }

    List<ParamRule> paramRules= JSON.parseArray(req.getParam().getCollectParam(),ParamRule.class);
    List<ColsDef> colsDefs=new ArrayList<>();

    for(ParamRule pr:paramRules){
      ColsDef cd=new ColsDef();
      cd.setTableTrueCol(pr.getName());
      cd.setUserDefCol(pr.getName());
      colsDefs.add(cd);
    }

    cdsd.setName(req.getParam().getCollectName()+"数据集");
    cdsd.setTableName(tableName);
    cdsd.setTableDef(JSON.toJSONString(colsDefs));
    cdsd.setScript("select * from `"+tableName+"` limit 100");
    cdsd.setUserId(req.getUser().getId());
    cdpDataSetDefMapper.insertSelective(cdsd);

    System.out.println(cdsd.getId());

    req.getParam().setDataSetDefId(cdsd.getId());
    req.getParam().setStatus(0);
    req.getParam().setUserId(req.getUser().getId());
    if(cdpCollectDefMapper.insertSelective(req.getParam())==1) {
      res.setMessage("添加成功");
    }else {
      res.setMessage("添加失败");
    }
    return res;
  }

  @Override
  public ResponseModel<CdpCollectDef> updateCdpCollectDef(RequestModel<CdpCollectDef> req) {
    //有三种操作，添加列，修改名称、删除列，需要针对不同的情况进行操作
    CdpDataSetDef cdsd=cdpDataSetDefMapper.selectByPrimaryKey(req.getParam().getDataSetDefId());

    List<ParamRule> paramRules= JSON.parseArray(req.getParam().getCollectParam(),ParamRule.class);
    List<ColsDef> colsDefs=new ArrayList<>();
    for(ParamRule pr:paramRules){
      ColsDef cd=new ColsDef();
      cd.setTableTrueCol(pr.getName());
      cd.setUserDefCol(pr.getName());
      colsDefs.add(cd);
    }
    cdsd.setName(req.getParam().getCollectName()+"数据集");
    cdsd.setTableDef(JSON.toJSONString(colsDefs));
    cdsd.setUserId(null);
    cdpDataSetDefMapper.updateByPrimaryKeySelective(cdsd);

    ResponseModel res=new ResponseModel();
    req.getParam().setOperateTime(new Date());
    req.getParam().setCreateTime(null);
    req.getParam().setDataSetDefId(null);
    req.getParam().setStatus(null);
    res.setStatus(200);

    if(cdpCollectDefMapper.updateByPrimaryKeySelective(req.getParam())==1) {
      res.setMessage("更新成功");
    }else {
      res.setMessage("更新失败");
    }
    return res;
  }

  @Override
  public ResponseModel<CdpCollectDef> getCollectDefById(RequestModel<CdpCollectDef> req) {
    ResponseModel res=new ResponseModel();
    res.setStatus(200);
    res.setData(cdpCollectDefMapper.selectByPrimaryKey(req.getParam().getId()));
    return res;
  }

  @Override
  @Transactional
  public ResponseModel<CdpCollectDef> deleteCollectDefByIds(DeleteModel dm) {
    ResponseModel res=new ResponseModel();
    res.setStatus(200);
    String ids="";
    if(dm!=null) {
      ids=  StringUtils.collectionToDelimitedString(dm.getIds(), ",");
    }
    List<CdpCollectDef> listCdpCollectDef=cdpCollectDefMapper.selectByIds(ids);
    List<String> dataSetIds=new ArrayList<>();
    String dataSetIdsStr="";
    for(CdpCollectDef ccd:listCdpCollectDef){
      dataSetIds.add(ccd.getDataSetDefId().toString());
    }
    if(dataSetIds.size()!=0){
      dataSetIdsStr=StringUtils.collectionToDelimitedString(dataSetIds, ",");
    }
    cdpDataSetDefMapper.deleteDataSetByIds(dataSetIdsStr);
    cdpCollectDefMapper.deleteCollectDefByIds(ids);

    return res;
  }

  @Override
  public ResponseModel<Map<String, String>> webmagicSyncRun(RequestModel<CdpCollectDef> req) {
    CdpDataSetDef cdsf=cdpDataSetDefMapper.selectByPrimaryKey(req.getParam().getDataSetDefId());

    webmagicService.webmagicSyncRun(req.getParam(),cdsf);
    ResponseModel<Map<String,String>> res=new ResponseModel<>();
    res.setStatus(200);
    res.setData(webmagicService.getResultData(cdsf.getTableName(),""));

    return res;
  }


  @Override
  public ResponseModel<Map<String, String>> webmagicAsyncStart(RequestModel<CdpCollectDef> req) {
    CdpCollectDef ccd=cdpCollectDefMapper.selectByPrimaryKey(req.getParam().getId());
    CdpDataSetDef cdsf=cdpDataSetDefMapper.selectByPrimaryKey(ccd.getDataSetDefId());

    CdpCollectDef updateStatus=new CdpCollectDef();
    updateStatus.setId(ccd.getId());
    updateStatus.setStatus(3);
    //3为正在执行
    cdpCollectDefMapper.updateCollectDefStatus(updateStatus);

    webmagicService.webmagicAsyncStart(ccd,cdsf,"manual");
    ResponseModel<Map<String,String>> res=new ResponseModel<>();
    res.setStatus(200);
    res.setMessage("执行成功");
    return res;
  }
}
