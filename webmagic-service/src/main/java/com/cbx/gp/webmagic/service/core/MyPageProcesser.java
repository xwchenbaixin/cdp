package com.cbx.gp.webmagic.service.core;

import com.alibaba.fastjson.JSON;
import com.cbx.gp.platform.pojo.entity.CdpCollectDef;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.entity.ColsDef;
import com.cbx.gp.platform.pojo.entity.ParamRule;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ListPageProcesser
 * @Description TODO
 * @Date 2020/4/27 23:35
 * @Created by CBX
 */
public class MyPageProcesser implements PageProcessor {

  private  final Logger logger = LoggerFactory.getLogger(MyPageProcesser.class);

  private Site site ;

  public static Set<String> urlSet=new HashSet<>();

  private CdpCollectDef ccd;

  private PriorityQueue<CdpCollectDef> collectDefQue;

  private List<ColsDef> colList;

  private String tableName;

  public MyPageProcesser(PriorityQueue<CdpCollectDef> collectDefQue, Site site, CdpCollectDef ccd, CdpDataSetDef cdsd){
    this.collectDefQue=collectDefQue;
    this.site=site;
    this.ccd=ccd;
    this.tableName=cdsd.getTableName();
    this.colList=JSON.parseArray(cdsd.getTableDef(),ColsDef.class);;

  }

  public void setCollectDefQue(PriorityQueue<CdpCollectDef> collectDefQue){
    this.collectDefQue=collectDefQue;
  }

  @Override
  public void process(Page page) {
    System.out.println(page.getUrl());

    JXDocument html=JXDocument.create(page.getHtml().toString());
    String nextUrl= null;
    nextUrl = getNextPageUrl(page);


    addNextPageUrl(nextUrl,page);

    List<Map<String,String>> dataList=new ArrayList<>();

    //循环数据域，数据域应该是个 数组
    List<JXNode> domaminNodes=html.selN(ccd.getDataDomain());

    List<ParamRule> collectParamList= JSON.parseArray(ccd.getCollectParam(),ParamRule.class);


    for(JXNode node:domaminNodes){
      Map<String,String> oneData=new HashMap<>();
      for(ParamRule pr : collectParamList){
        if(pr.getDataType()==0 && pr.getXpath()!=""){
          String dataValue=node.selOne(pr.getXpath()).toString();
          oneData.put(pr.getName(),dataValue);
        }
        else{
          //其他数据类型处理，如img
        }
      }

      dataList.add(oneData);
    }
    page.putField("data",dataList);
    page.putField("collectDef",ccd);
    page.putField("colList",colList);
    page.putField("tableName",tableName);
  }

  @Override
  public Site getSite() {
    return site;
  }

  public List<String> getNowPageUrls(String regex,List<String> pageUrls,Page page){


    List<String> nowPageUrl=new ArrayList<>();
    Pattern p=Pattern.compile(regex);
    for(String url:pageUrls){
      url=url.trim();
      if(url==null||url=="")
        continue;
      Html html=new Html(url);
      String a=html.$("a","href").toString();
      url=a==null?url:a;

      Matcher m=p.matcher(url);
      if(m.find()){
        nowPageUrl.add(site.getDomain()+m.group(0));
      }
    }
    return nowPageUrl;
  }

  public void addPageUrls(List<String> pageUrls,Page page){
    Integer nowLevel=(Integer) page.getRequest().getExtra("nextPageTotal");
    logger.warn("nowPage:"+nowLevel);
    for(String url:pageUrls){

        Request request = new Request(url).putExtra("nextPageTotal", (nowLevel + 1));
        page.getTargetRequests().add(request);

    }
  }

  public void addNextPageUrl(String nextPageUrl,Page page){
    if(nextPageUrl!=null) {
      Integer nowLevel = (Integer) page.getRequest().getExtra("nextPageTotal");

      logger.warn("nowPage:" + nowLevel);


      Request request = new Request(nextPageUrl).putExtra("nextPageTotal", (nowLevel + 1));
      //request.setMethod("GET");
      //WebMagicUtils.setHeader(request, ccd.getHeaders());
      page.getTargetRequests().add(request);
    }else {
      logger.warn("下一页地址为null");
    }

  }

  public Boolean addUrlSet(String url){
    if(urlSet.contains(url)) {
      return false;
    }
    else{
      urlSet.add(url);
      return true;
    }

  }


  public String getNextPageUrl(Page page) {
    Html html=page.getHtml();

    JXDocument jxDocument = JXDocument.create(html.toString());

    String url=null;

    //获取下一页的URL
    if(ccd.getNextPageType()==0){
      //0为链接是URL类型
      //根据xpath获取下一页URL的链接。
      //先判断，nextPage的类型 ,0-xpath，1-url，2-按钮，3-递归参数表达式
      if(ccd.getNextPageType()==0){
        //Selectable selectable= ;
        url=jxDocument.selOne(ccd.getNextPage()).toString();
        String baseUrl=page.getRequest().getUrl();
        url= WebMagicUtils.getAbsoluteURL(baseUrl,url);
      }
      else if(ccd.getNextPageType()==1){

      }
      else if(ccd.getNextPageType()==2){

      }
      else if(ccd.getNextPageType()==3){

      }

    }else if(ccd.getNextPageType()==1){
      //1为Button类型

    }
    return url;
  }

}
