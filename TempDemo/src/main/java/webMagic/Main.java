package webMagic;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import webMagic.MyPageProcesser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

    Site site = Site.me().setDomain("https://www.dytt8.net");

    PriorityQueue<CdpCollectDef> collectDefQue=new PriorityQueue<>();

    CdpCollectDef ccd=JSON.parseObject(readFile(),CdpCollectDef.class);
    System.out.println(ccd);

    LevelLimitScheduler levelLimitScheduler = new LevelLimitScheduler(ccd.getNextPageTotal());

    // 设置布隆过滤器去重操作（默认使用HashSet来进行去重，占用内存较大；使用BloomFilter来进行去重，占用内存较小，但是可能漏抓页面）
    Spider spider = Spider.create(new MyPageProcesser(collectDefQue,site,ccd))
            .addPipeline(new MyPipeline())
            .setScheduler(levelLimitScheduler.
                    setDuplicateRemover(new BloomFilterDuplicateRemover(10000000))
            )
            .thread(5);

    Request request=
            new Request(ccd.getCollectUrl())
            .setPriority(2)//设置线程优先级
            .putExtra("nextPageTotal", 1);

    levelLimitScheduler.push(request, spider);

    spider.run();
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
