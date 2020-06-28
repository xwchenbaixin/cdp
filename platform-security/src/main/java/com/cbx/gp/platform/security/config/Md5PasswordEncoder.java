package com.cbx.gp.platform.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @Classname Md5PasswordEncoder
 * @Description TODO
 * @Date 2020/6/6 2:06
 * @Created by CBX
 */
public class Md5PasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawPassword) {
    return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encodedPassword.equals(DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()));
  }
}
