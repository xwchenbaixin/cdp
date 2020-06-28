package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpOrder;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;

import java.util.Map;

/**
 * @Classname OrderManagerService
 * @Description TODO
 * @Date 2020/6/2 0:42
 * @Created by CBX
 */
public interface OrderManagerService {
  String aliPay(RequestModel<CdpOrder> req);

  ResponseModel<CdpOrder> cancelOrder(RequestModel<CdpOrder> req);

  ResponseModel<CdpOrder> listOrders(RequestModel<CdpOrder> req);

  ResponseModel<CdpOrder> aliPlayCallback(Map<String, String> params);
}
