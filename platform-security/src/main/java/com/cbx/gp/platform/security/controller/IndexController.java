package com.cbx.gp.platform.security.controller;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.bean.TokenBean;
import com.cbx.gp.platform.pojo.bean.UserInfo;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.pojo.utils.RSAUtils;
import com.cbx.gp.platform.security.dao.RedisOperate;
import com.cbx.gp.platform.security.model.User;
import com.cbx.gp.platform.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class IndexController {
    private Jedis redis =RedisOperate.redis;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @RequestMapping("/loginfff")
    @ResponseBody
    public ResponseModel<CdpUser> login(String username, String password) {
        System.out.println("fff");
        ResponseModel<CdpUser> res=new ResponseModel<>();
        CdpUser user=  new CdpUser();
        user.setUsername(username);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        String regex="^[a-zA-Z]\\w{5,17}$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(password);
        if(!m.find()){
            res.setStatus(402);
            res.setMessage("密码不符合规则");
            return res;
        }
        if(!user.getUsername().equals(username)){
            res.setStatus(401);
            res.setMessage("用户名不存在");
            return  res;
        }
        if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            res.setStatus(401);
            res.setMessage("密码不正确");
            return  res;
        }
        //登录的时候判断有无用户信息，如果已经存在，则从redis中获取用户信息，并且验证用户名密码是否正确
        if(redis.exists(username)){
            //登录
            Map<String,String> userDataMap = redis.hgetAll(username);
            userDataMap.put("username",username);
            userDataMap.put("password",user.getPassword());
            String pubKey=userDataMap.get("pubKey");
            String userDetailJson= JSON.toJSONString(user);

            byte[] data = userDetailJson.getBytes();
            byte[] encodedData = null;

            try {
                encodedData = RSAUtils.encryptByPublicKey(data, pubKey);
            } catch (Exception e) {
                res.setStatus(500);
                res.setMessage(e.getMessage());
                e.printStackTrace();
                return res;
            }
            String token=new String(encodedData);
            userDataMap.put("token",token);
            //设置过期时间为5小时
            redis.expire(username,5*60*60);
            //更新user信息
            redis.hmset(username,userDataMap);

            res.setStatus(200);
            res.setToken(token);
            res.setMessage("登录成功");
        }else {
            //如果redis中不存在用户信息
            try {
                Map<String,String> userDataMap = new HashMap<>();
                Map<String, Object> keyPair = RSAUtils.genKeyPair();
                String pubKey=RSAUtils.getPublicKey(keyPair);
                String privateKey=RSAUtils.getPrivateKey(keyPair);
                String userDetailJson= JSON.toJSONString(user);

                byte[] data = userDetailJson.getBytes();
                byte[] encodedData = null;

                try {
                    encodedData = RSAUtils.encryptByPublicKey(data, pubKey);
                } catch (Exception e) {
                    res.setStatus(500);
                    res.setMessage(e.getMessage());
                    e.printStackTrace();
                    return res;
                }
                String token=new String(encodedData);
                userDataMap.put("pubKey",pubKey);
                userDataMap.put("privateKey",privateKey);
                userDataMap.put("token",token);
                redis.hmset(username, userDataMap);
                //过期时间设置为5小时
                redis.expire(username, 5*60*60);
                res.setStatus(200);
                res.setToken(token);
                res.setMessage("登录成功");
            } catch (Exception e) {
                res.setStatus(500);
                res.setMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        return res;
    }



    @RequestMapping("/admin")
    @ResponseBody
    public String admin(){
        System.out.println("wellcome admin");
        return "ADMIN";
    }


    @RequestMapping("/free")
    @ResponseBody
    public String free(){
        System.out.println("wellcome free");
        return "free";
    }

}
