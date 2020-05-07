package webMagic;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

/**
 * @Classname LevelLimitScheduler
 * @Description TODO
 * @Date 2020/4/28 10:55
 * @Created by CBX
 */
public class LevelLimitScheduler extends PriorityScheduler {

  private int levelLimit = 0;

  public LevelLimitScheduler(int levelLimit) {
    this.levelLimit = levelLimit;
  }

  @Override
  public synchronized void push(Request request, Task task) {
    Integer nowLevel=((Integer) request.getExtra("nextPageTotal"));
    if ( nowLevel!=null&&nowLevel<= levelLimit) {
      logger.info("nextPageTotal:"+((Integer) request.getExtra("nextPageTotal")).toString()+",,,,url:"+request.getUrl());
      super.push(request, task);
    }
  }
}