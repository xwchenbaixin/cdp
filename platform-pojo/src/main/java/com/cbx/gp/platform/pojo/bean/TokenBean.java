package com.cbx.gp.platform.pojo.bean;

import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.pojo.utils.RSAUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @Classname TokenBean
 * @Description TODO
 * @Date 2020/6/3 22:50
 * @Created by CBX
 */
public class TokenBean implements Serializable {
  public String publicKey;
  public String privateKey;
  public String token;
  public CdpUser user;

  public TokenBean(){

  }

  public TokenBean(String userInfo,CdpUser user){
    try {
      Map<String, Object> keyPair = RSAUtils.genKeyPair();
      publicKey = RSAUtils.getPublicKey(keyPair);
      privateKey = RSAUtils.getPrivateKey(keyPair);

      byte[] data = userInfo.getBytes();
      byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
      token=new String(encodedData);
      this.user=user;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



}
