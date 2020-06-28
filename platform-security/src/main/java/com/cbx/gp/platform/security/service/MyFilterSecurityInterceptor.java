package com.cbx.gp.platform.security.service;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.security.dao.RedisOperate;
import com.cbx.gp.platform.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author CBX
 */
@Component
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
  int i = 0;
  @Autowired
  private FilterInvocationSecurityMetadataSource securityMetadataSource;

  @Autowired
  public void setMyAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager) {
    super.setAccessDecisionManager(myAccessDecisionManager);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    Jedis redis= new Jedis("192.168.80.242", 6379,0);
    ResponseModel<User> resData=new ResponseModel<>();
    //校验权限
    HttpServletRequest req =(HttpServletRequest) request;
    System.out.println(req.getRequestURI());

    System.out.println("Authorization:"+req.getHeader("Authorization"));
    for (Map.Entry<String,String[]> e: req.getParameterMap().entrySet()) {
      System.out.println("param:key:" + e.getKey()+"    val:"+e.getValue());
    }

    String token=req.getHeader("Authorization");
    Object object=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user=null;
    if(object instanceof User){
      user=(User) object;
    }

    if(user!=null) {
      Map<String, String> keyMap = redis.hgetAll(user.getUsername());
      if (keyMap != null && token != null) {
        try {
          String privateKey = keyMap.get("privateKey");
          Algorithm algorithm = Algorithm.HMAC256(privateKey);
          JWTVerifier verifier = JWT.require(algorithm).build();
          DecodedJWT jwt= verifier.verify(token);
          String username = jwt.getClaim("username").asString();
          if (!username.equals(user.getUsername())) {
            resData.setStatus(401);
            resData.setMessage("请重新登录");
          } else {
            resData.setStatus(200);
          }
        }catch (Exception e){
          resData.setStatus(401);
          resData.setMessage("重新登录");
          e.printStackTrace();
        }
      } else {
        resData.setStatus(401);
        resData.setMessage("请重新登录");
      }
    }else{
      resData.setStatus(401);
      resData.setMessage("请重新登录");
    }
    redis.close();
    HttpServletResponse servletResponse=(HttpServletResponse)response;
    if (resData.getStatus()!=200){

      servletResponse.setStatus(resData.getStatus());
      servletResponse.setHeader("Authorization",null);
      servletResponse.setCharacterEncoding("utf-8");
      servletResponse.setContentType("application/json; charset=utf-8");
      PrintWriter writer = servletResponse.getWriter();
      writer.write(JSON.toJSONString(resData));
      return;
    }

    FilterInvocation fi = new FilterInvocation(request, response, chain);
    invoke(fi);
  }


  public void invoke(FilterInvocation fi) throws IOException, ServletException {

    //fi里面有一个被拦截的url
    //里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
    //再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够

    InterceptorStatusToken token = super.beforeInvocation(fi);
    System.out.println("url:" + fi.getRequest().getRequestURL() + ",I:" + i++);
    try {
      //执行下一个拦截器
      fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
    } finally {
      super.afterInvocation(token, null);
    }

  }

  @Override
  public void destroy() {

  }

  @Override
  public Class<?> getSecureObjectClass() {
    return FilterInvocation.class;
  }

  @Override
  public SecurityMetadataSource obtainSecurityMetadataSource() {
    return this.securityMetadataSource;
  }
}