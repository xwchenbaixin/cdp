package webMagic;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.ParamRule;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;

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

  public WebMagicStart(CdpCollectDef ccd){

    PriorityQueue<CdpCollectDef> collectDefQue=new PriorityQueue<>();

    Site site = Site.me().setDomain(WebMagicUtils.getDomain(ccd.getCollectUrl()));

    LevelLimitScheduler levelLimitScheduler = new LevelLimitScheduler(ccd.getNextPageTotal());

    //在数据集中创建表
    List<ParamRule> collectParamList= JSON.parseArray(ccd.getCollectParam(),ParamRule.class);


    // 设置布隆过滤器去重操作（默认使用HashSet来进行去重，占用内存较大；使用BloomFilter来进行去重，占用内存较小，但是可能漏抓页面）
    spider = Spider.create(new MyPageProcesser(collectDefQue,site,ccd))
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

    WebMagicUtils.setHeader(request,ccd.getHeaders());
  }

  public Spider run(){
    DataSet.dataList.clear();
    this.spider.run();
    return spider;
  }

}
