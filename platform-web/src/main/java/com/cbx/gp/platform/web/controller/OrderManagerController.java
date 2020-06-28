package com.cbx.gp.platform.web.controller;

import com.cbx.gp.platform.pojo.entity.CdpOrder;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.OrderManagerService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Classname OrderManagerController
 * @Description TODO
 * @Date 2020/6/2 0:43
 * @Created by CBX
 */
@RestController
@RequestMapping("/orderManager")
public class OrderManagerController {

  @Reference(retries = -1,timeout = 10000)
  private OrderManagerService orderManagerService;

  @RequestMapping("/createNewOrder")
  public String createNewOrder(@RequestBody RequestModel<CdpOrder> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);

    if(req.getParam()==null){
      req.setParam(new CdpOrder());
    }
    req.getParam().setOrderName("10元升级套餐");
    req.getParam().setOrderType(0);
    req.getParam().setAmount(10.00);
    req.getParam().setStatus(0);
    return orderManagerService.aliPay(req);
  }

  @RequestMapping("/listOrders")
  public ResponseModel<CdpOrder> listOrders(@RequestBody RequestModel<CdpOrder> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return orderManagerService.listOrders(req);
  }

  @RequestMapping("/cancelOrder")
  public ResponseModel<CdpOrder> cancelOrder(@RequestBody RequestModel<CdpOrder> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return orderManagerService.cancelOrder(req);
  }
  @RequestMapping("/aliPlayCallback")
  public ResponseModel<CdpOrder> cancelOrder(HttpServletRequest request){

    Map<String, String> retMap = new HashMap<String, String>();

    Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

    for (Map.Entry<String, String[]> entry : entrySet) {
      String name = entry.getKey();
      String[] values = entry.getValue();
      int valLen = values.length;

      if (valLen == 1) {
        retMap.put(name, values[0]);
      } else if (valLen > 1) {
        StringBuilder sb = new StringBuilder();
        for (String val : values) {
          sb.append(",").append(val);
        }
        retMap.put(name, sb.toString().substring(1));
      } else {
        retMap.put(name, "");
      }
    }
    return orderManagerService.aliPlayCallback(retMap);
  }

}
