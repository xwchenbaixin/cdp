package com.cbx.gp.platform.service.impl;

//import com.alibaba.dubbo.config.annotation.Service;
import org.apache.dubbo.config.annotation.Service;
import com.cbx.gp.platform.dao.mapper.CdpDataSetDefMapper;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDefExample;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import com.cbx.gp.platform.service.interfaces.DataSetService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname DataSetServiceImpl
 * @Description TODO
 * @Date 2020/5/11 13:42
 * @Created by CBX
 */
@Service
public class DataSetServiceImpl implements DataSetService {

  @Autowired
  private CdpDataSetDefMapper cdpDataSetDefMapper;

  public ResponseModel<CdpDataSetDef> selectByPrimaryKey(Integer id){
    ResponseModel<CdpDataSetDef> res=new ResponseModel<>();
    res.setData(cdpDataSetDefMapper.selectByPrimaryKey(id));
    res.setStatus(200);
    return res;
  }


  public ResponseModel<CdpDataSetDef> listDataSet(RequestModel<CdpDataSetDef> req){
    ResponseModel<CdpDataSetDef> res=new ResponseModel<>();
    CdpDataSetDefExample cdsde=new CdpDataSetDefExample();
    CdpDataSetDefExample.Criteria criteria = cdsde.createCriteria();
    criteria.andUserIdEqualTo(req.getUser().getId());
    if(req.getParam()!=null&&req.getParam().getName()!=null)
      criteria.andNameLike("%"+req.getParam().getName()+"%");

    PageHelper.startPage(req.getPagination().getCurrPage(),req.getPagination().getPageSize());
    PageInfo<CdpDataSetDef> pageInfo=new PageInfo<>(cdpDataSetDefMapper.selectByExample(cdsde));

    res.setTotal(pageInfo.getTotal());
    res.setStatus(200);
    res.setData(pageInfo.getList());
    return res;

  }

  @Override
  public ResponseModel<CdpDataSetDef> saveScript(RequestModel<CdpDataSetDef> req) {
    ResponseModel<CdpDataSetDef> res=new ResponseModel<>();
    cdpDataSetDefMapper.updateByPrimaryKeySelective(req.getParam());
    res.setStatus(200);
    res.setMessage("保存成功");
    return res;
  }
}
