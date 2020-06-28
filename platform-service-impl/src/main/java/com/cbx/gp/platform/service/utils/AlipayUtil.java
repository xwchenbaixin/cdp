package com.cbx.gp.platform.service.utils;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cbx.gp.platform.pojo.bean.AlipayBean;
import com.cbx.gp.platform.service.ApplyConfig;

/**
 * @Classname AlipayUtil
 * @Description TODO
 * @Date 2020/6/1 22:55
 * @Created by CBX
 */
/* 支付宝 */
public class AlipayUtil {
    public static String connect(AlipayBean alipayBean) throws AlipayApiException {
         //1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                ApplyConfig.getKey("gatewayUrl"),//支付宝网关
                ApplyConfig.getKey("app_id"),//appid
                ApplyConfig.getKey("merchant_private_key"),//商户私钥
                           "json",
                ApplyConfig.getKey("charset"),//字符编码格式
                ApplyConfig.getKey("alipay_public_key"),//支付宝公钥
                ApplyConfig.getKey("sign_type")//签名方式
        );
        //2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(ApplyConfig.getKey("return_url"));
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(ApplyConfig.getKey("notify_url"));
        //封装参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayBean));
        //3、请求支付宝进行付款，并获取支付结果
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        //返回付款信息
        return  result;
    }
}
