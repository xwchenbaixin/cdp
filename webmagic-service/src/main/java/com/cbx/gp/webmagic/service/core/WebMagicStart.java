package com.cbx.gp.webmagic.service.core;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.entity.ColsDef;
import com.cbx.gp.platform.pojo.entity.ParamRule;
import com.cbx.gp.webmagic.service.databaseManager.DwDatabaseManager;
import com.cbx.gp.webmagic.service.databaseManager.HiveDwDatabaseManager;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @Classname WebMagicStart
 * @Description TODO
 * @Date 2020/5/9 12:46
 * @Created by CBX
 */
public class WebMagicStart {
  private Spider spider;
  public WebMagicStart(CdpCollectDef ccd, CdpDataSetDef cdsd){

    PriorityQueue<CdpCollectDef> collectDefQue=new PriorityQueue<>();

    Site site = Site.me().setDomain(WebMagicUtils.getDomain(ccd.getCollectUrl()));

    site.setRetryTimes(5000);
    site.setSleepTime(1000);
    site.setTimeOut(30000);

    WebMagicUtils.setHeader(site,ccd.getHeaders());

    LevelLimitScheduler levelLimitScheduler = new LevelLimitScheduler(ccd.getNextPageTotal());



    //每次爬取数据前清理表
    try {
      Connection con= HiveDwDatabaseManager.getConnection();
      //删除表重新写创建
      con.prepareStatement("drop table if exists cdp_dw.`"+cdsd.getTableName()+"`").execute();
      //每次执行前创建表结构，如果已经创建了则不用创建
      List<ParamRule> paramRules= JSON.parseArray(ccd.getCollectParam(),ParamRule.class);
      List<ColsDef> colsDefs=new ArrayList<>();

      StringBuilder sb=new StringBuilder();
      sb.append("CREATE TABLE if not exists cdp_dw.`"+cdsd.getTableName() +"`(");
      for(int i=0;i<paramRules.size();i++){
        ParamRule pr=paramRules.get(i);
        ColsDef cd=new ColsDef();
        cd.setTableTrueCol(pr.getName());
        cd.setUserDefCol(pr.getName());

        sb.append("`"+pr.getName()+"` string ,");
        colsDefs.add(cd);
      }
      sb.delete(sb.length()-1,sb.length());
      sb.append(")");
      con.prepareStatement(sb.toString()).execute();
      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // 设置布隆过滤器去重操作（默认使用HashSet来进行去重，占用内存较大；使用BloomFilter来进行去重，占用内存较小，但是可能漏抓页面）
    spider = Spider.create(new MyPageProcesser(collectDefQue,site,ccd,cdsd))
            .addPipeline(new MyPipeline())
            .setScheduler(levelLimitScheduler
                    .setDuplicateRemover(new BloomFilterDuplicateRemover(10000000))
            )
            .thread(5);

    Request request=
            new Request(ccd.getCollectUrl())
            .setPriority(2)//设置线程优先级
                    //从1当前页开始
            .putExtra("nextPageTotal",1);

    levelLimitScheduler.push(request, spider);

  }

  public Spider getSpider(){
    return spider;
  }

  public void asyncStart(){

  }


}
