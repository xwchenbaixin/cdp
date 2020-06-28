package com.cbx.gp.platform.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.pojo.utils.RSAUtils;
import com.cbx.gp.platform.security.dao.RedisOperate;
import com.cbx.gp.platform.security.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	private Jedis redis = RedisOperate.redis;
	private static final int EXPIRE_TIME = 5*60*60*1000;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
//		if(!redis.isConnected()){
//			redis= new Jedis("192.168.80.242", 6379,0);
//		}
		Jedis redis= new Jedis("192.168.80.242", 6379,0);
		User user= (User) authentication.getPrincipal();
		String username=user.getUsername();

		ResponseModel<User> res=new ResponseModel<>();

    //设置CDPUser
    CdpUser cdpUser=new CdpUser();
    cdpUser.setUsername(user.getUsername());
    cdpUser.setId(user.getId());
    cdpUser.setPhone(user.getPhone());
    cdpUser.setEmail(user.getEmail());
    cdpUser.setType(user.getType());

		//登录的时候判断有无用户信息，如果已经存在，则从redis中获取用户信息，并且验证用户名密码是否正确

		//每次登录重新生成privatekey
		try {
			Map<String,String> userDataMap = new HashMap<>();
			Map<String, Object> keyPair = RSAUtils.genKeyPair();
			String pubKey=RSAUtils.getPublicKey(keyPair);
			String privateKey=RSAUtils.getPrivateKey(keyPair);
			String userDetailJson= JSON.toJSONString(user);

			// 设置过期时间
			Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
			// 私钥和加密算法
			Algorithm algorithm = Algorithm.HMAC256(privateKey);
			// 设置头部信息
			Map<String, Object> header = new HashMap<>(2);
			header.put("Type", "Jwt");
			header.put("alg", "HS256");
			// 返回token字符串
			String token=JWT.create()
							.withHeader(header)
							.withClaim("username", username)
							.withExpiresAt(date)
							.sign(algorithm);

			userDataMap.put("pubKey",pubKey);
			userDataMap.put("privateKey",privateKey);
			userDataMap.put("token",token);

			redis.hmset(username, userDataMap);

			//过期时间设置为5小时
			redis.expire(username, EXPIRE_TIME);
			res.setStatus(200);
			res.setToken(token);
			res.setData(user);
			res.setMessage("登录成功");
		} catch (Exception e) {
			res.setStatus(500);
			res.setMessage(e.getMessage());
			e.printStackTrace();
		}

		redis.close();
    //设置session
		request.getSession().setAttribute("user",cdpUser);

		response.setHeader("token",res.getToken());
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(JSON.toJSONString(res));

	}

}
