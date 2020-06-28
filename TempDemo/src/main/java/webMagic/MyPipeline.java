package webMagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MyPipeline
 * @Description TODO
 * @Date 2020/4/27 23:37
 * @Created by CBX
 */

public class MyPipeline implements Pipeline {
  private  final Logger logger = LoggerFactory.getLogger(MyPipeline.class);

  @Override
  public void process(ResultItems resultItems, Task task) {

    //Map<String,String> pageData=new HashMap<>();
    for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
      //pageData.put(entry.getKey(), (String) entry.getValue());
      logger.info(entry.getKey() + ":\t" + entry.getValue());
    }
    //DataSet.dataList.add(pageData);
  }
}
