package com.cbx.gp.platform.security.dao;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @Classname RedisOperate
 * @Description TODO
 * @Date 2020/6/4 1:34
 * @Created by CBX
 */
@Component
public class RedisOperate {
  public static  Jedis redis = new Jedis("192.168.80.242", 6379,0);

}
