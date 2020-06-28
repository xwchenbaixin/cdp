package com.cbx.gp.webmagic.service.core;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;

import java.io.*;


/**
 * @Classname Main
 * @Description TODO
 * @Date 2020/4/27 23:45
 * @Created by CBX
 */
public class Main {


  public static void main(String[] args) {
    //获取影片标题和页面链接


    CdpCollectDef ccd=JSON.parseObject(readFile("ccd"),CdpCollectDef.class);
    CdpDataSetDef cdsd=JSON.parseObject(readFile("cdsd"),CdpDataSetDef.class);

    new WebMagicStart(ccd,cdsd).getSpider().run();

  }


  public static String readFile(String name){
    try {
      InputStreamReader isr = new InputStreamReader(new FileInputStream("./webmagic-client/src/main/java/com/cbx/gp/webmagic/client/"+name+".txt"), "UTF-8");

      BufferedReader br=new BufferedReader(isr);
      String line="";
      String text="";
      while((line=br.readLine())!=null){
        text+=line;
      }
      br.close();
      System.out.println(text);
      return text;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
