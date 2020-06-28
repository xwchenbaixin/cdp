package com.cbx.gp.platform.dao;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname GeneratorStart
 * @Description TODO
 * @Date 2020/4/23 12:16
 * @Created by CBX
 */
public class GeneratorStart {
  public static void main(String[] args) throws Exception{

    List<String> warnings = new ArrayList<String>();
    boolean overwrite = true;
    // 从这里加载配置文件，此时要注意路径
    // 其他的代码不用动，直接运行即可
    File configFile = new File("./platform-dao/src/main/resources/generatorConfig.xml");
    ConfigurationParser cp = new ConfigurationParser(warnings);

    Configuration config = cp.parseConfiguration(configFile);
    DefaultShellCallback callback = new DefaultShellCallback(overwrite);
    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
    myBatisGenerator.generate(null);
    System.out.println(warnings);
  }
}
