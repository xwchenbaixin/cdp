package webMagic;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Request;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * @Classname WebMagicUtils
 * @Description TODO
 * @Date 2020/5/3 9:54
 * @Created by CBX
 */
public class WebMagicUtils {
  public static String getAbsoluteURL(String baseURI, String relativePath){
    String abURL=null;
    try {
      URI base=new URI(baseURI);//基本网页URI
      URI abs=base.resolve(relativePath);//解析于上述网页的相对URL，得到绝对URI
      URL absURL=abs.toURL();//转成URL
      abURL = absURL.toString();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } finally{
      return abURL;
    }
  }

  public static String getDomain(String url){
    String domain=null;
    try {
      domain=new URI(url).getHost();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return domain;
  }

  public static void setHeader(Request request,String headers){
    if(headers==null || headers.equals("")){
      return ;
    }
    Map<String,String> headersMap= JSON.parseObject(headers,Map.class);
    for(Map.Entry<String,String> e:headersMap.entrySet()){
      System.out.println(e.getKey()+"             "+e.getValue());
    }
  }


}
