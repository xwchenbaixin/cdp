package com.cbx.gp.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cbx.gp.platform.dao.mapper.CdpOrderMapper;
import com.cbx.gp.platform.dao.mapper.CdpUserMapper;
import com.cbx.gp.platform.pojo.bean.AlipayBean;
import com.cbx.gp.platform.pojo.entity.CdpOrder;
import com.cbx.gp.platform.pojo.entity.CdpOrderExample;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.param.AlipayNotifyParam;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.ApplyConfig;
import com.cbx.gp.platform.service.interfaces.OrderManagerService;
import com.cbx.gp.platform.service.utils.AlipayUtil;
import com.cbx.gp.platform.service.utils.OrderCodeFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Classname OrderManagerServiceImpl
 * @Description TODO
 * @Date 2020/6/1 21:58
 * @Created by CBX
 */
@Service
@Transactional
public class OrderManagerServiceImpl implements OrderManagerService {
  private static Logger logger = LoggerFactory.getLogger(OrderManagerServiceImpl.class);
  private ExecutorService executorService = Executors.newFixedThreadPool(20);

  @Autowired
  private CdpOrderMapper cdpOrderMapper;

  @Autowired
  private CdpUserMapper cdpUserMapper;

  @Override
  public String aliPay(RequestModel<CdpOrder> req){
    ResponseModel<CdpOrder> res=new ResponseModel<>();
    AlipayBean ab=new AlipayBean();
    ab.setOut_trade_no(OrderCodeFactory.getOrderCode(System.currentTimeMillis()));
    ab.setSubject(req.getParam().getOrderName());
    if(req.getParam().getOrderType()==0){
      ab.setTotal_amount(new StringBuffer(req.getParam().getAmount().toString()));
    }
    String result=null;
    try {
      result=AlipayUtil.connect(ab);
    } catch (AlipayApiException e) {
      e.printStackTrace();
    }
    System.out.println(ab.getOut_trade_no());
    req.getParam().setStatus(0);
    req.getParam().setId(ab.getOut_trade_no());
    req.getParam().setUserId(req.getUser().getId());
    cdpOrderMapper.insertSelective(req.getParam());

    return result;
  }

  @Override
  public ResponseModel<CdpOrder> cancelOrder(RequestModel<CdpOrder> req){
    ResponseModel<CdpOrder> res=new ResponseModel<>();

    return res;
  }

  @Override
  public ResponseModel<CdpOrder> listOrders(RequestModel<CdpOrder> req){
    ResponseModel<CdpOrder> res=new ResponseModel<>();
    CdpOrder cdpOrder=req.getParam();
    CdpOrderExample coe=new CdpOrderExample();
    CdpOrderExample.Criteria criteria = coe.createCriteria();
    criteria.andUserIdEqualTo(req.getUser().getId());
    if(cdpOrder!=null&&cdpOrder.getOrderName()!=null){
      criteria.andOrderNameLike("%"+cdpOrder.getOrderName()+"%");
    }
    if(cdpOrder!=null&&cdpOrder.getId()!=null){
      criteria.andIdLike("%"+cdpOrder.getId()+"%");
    }
    PageHelper.startPage(req.getPagination().getCurrPage(),req.getPagination().getPageSize());
    PageInfo<CdpOrder> pageInfo=new PageInfo<>(cdpOrderMapper.selectByExample(coe));
    res.setData(pageInfo.getList());
    res.setTotal(pageInfo.getTotal());
    res.setStatus(200);
    return res;
  }

  /**
   * <pre>
   * 第一步:验证签名,签名通过后进行第二步
   * 第二步:按一下步骤进行验证
   * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
   * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
   * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
   * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
   * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
   * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
   * </pre>
   *
   * @param params
   * @return
   */
  @Override
  @Transactional
  public ResponseModel<CdpOrder> aliPlayCallback(Map<String, String> params) {
    ResponseModel<CdpOrder> res=new ResponseModel<>();
    String paramsJson = JSON.toJSONString(params);
    String outTradeNo = params.get("out_trade_no");
    //获取支付宝

    CdpOrder order = cdpOrderMapper.selectByPrimaryKey(outTradeNo);
    CdpOrder updateOrder=new CdpOrder();
    updateOrder.setId(order.getId());
    //1为支付成功
    updateOrder.setStatus(1);
    updateOrder.setPayTime(new Date());
    cdpOrderMapper.updateByPrimaryKeySelective(updateOrder);

    //更新用户状态
    CdpUser cdpUser=new CdpUser();
    cdpUser.setType(2);
    cdpUser.setId(order.getUserId());
    cdpUserMapper.updateByPrimaryKeySelective(cdpUser);

    res.setStatus(200);
    res.setMessage("付款成功");
    return res;
    //因为没有公网IP，无法异步调用此接口，所以模拟成功支付的情况

    /*logger.info("支付宝回调，{}", paramsJson);
    try {
      // 调用SDK验证签名
      boolean signVerified = AlipaySignature.rsaCheckV1(params, ApplyConfig.getKey("alipay_public_key"),
              ApplyConfig.getKey("charset"), ApplyConfig.getKey("sign_type"));
      if (signVerified) {
        logger.info("支付宝回调签名认证成功");
        // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
        this.check(params);
        AlipayNotifyParam param = buildAlipayNotifyParam(params);
        String trade_status = param.getTradeStatus();

        // 支付成功
        if (trade_status.equals("TRADE_SUCCESS")
                || trade_status.equals("TRADE_FINISHED")) {
          // 处理支付成功逻辑
          try {
            logger.error("支付成功");
                                *//*
                                    // 处理业务逻辑。。。
                                    myService.process(param);
                                *//*

          } catch (Exception e) {
            logger.error("支付宝回调业务处理报错,params:" + paramsJson, e);
          }
        } else {
          logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}",trade_status,paramsJson);
        }

        // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
        // 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
        res.setStatus(200);
        res.setMessage("success");
        return res;
      } else {
        logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
        res.setStatus(500);
        res.setMessage("failure");
        return res;
      }
    } catch (AlipayApiException e) {
      logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", paramsJson, e.getMessage());
      res.setStatus(500);
      res.setMessage("failure");
      return res;
    }*/
  }


  private AlipayNotifyParam buildAlipayNotifyParam(Map<String, String> params) {
    String json = JSON.toJSONString(params);
    return JSON.parseObject(json, AlipayNotifyParam.class);
  }

  /**
   * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
   * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
   * 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
   * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
   * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
   * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
   *
   * @param params
   * @throws AlipayApiException
   */
  private void check(Map<String, String> params) throws AlipayApiException {
    String outTradeNo = params.get("out_trade_no");
    // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
    CdpOrder order = cdpOrderMapper.selectByPrimaryKey(outTradeNo); // 这个方法自己实现
    if (order == null) {
      throw new AlipayApiException("out_trade_no错误");
    }
    // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
    //long total_amount = new BigDecimal(params.get("total_amount")).multiply(new BigDecimal(100)).longValue();
    String total_amount=params.get("total_amount").toString();
    if (!total_amount.equals(String.format("%.2f", order.getAmount()))) {
      throw new AlipayApiException("error total_amount");
    }
    // 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
    // 第三步可根据实际情况省略
    // 4、验证app_id是否为该商户本身。
    if (!params.get("app_id").equals(ApplyConfig.getKey("app_id"))) {
      throw new AlipayApiException("app_id不一致");
    }
  }

}
