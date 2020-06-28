package webMagic;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;

import java.io.*;
import java.util.PriorityQueue;


/**
 * @Classname Main
 * @Description TODO
 * @Date 2020/4/27 23:45
 * @Created by CBX
 */
public class Main {


  public static void main(String[] args) {
    //获取影片标题和页面链接



    CdpCollectDef ccd=JSON.parseObject(readFile(),CdpCollectDef.class);

    new WebMagicStart(ccd).run();

  }


  public static String readFile(){
    try {
      InputStreamReader isr = new InputStreamReader(new FileInputStream("./TempDemo/src/main/java/webMagic/TestJson.txt"), "UTF-8");

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
