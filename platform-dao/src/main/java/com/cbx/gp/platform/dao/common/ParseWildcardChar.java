package com.cbx.gp.platform.dao.common;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ParseWildcardChar
 * @Description TODO
 * @Date 2020/4/13 10:24
 * @Created by CBX
 * 格式说明： 1、开启通配符以后，支持[YYYYMMDD],[GLR]的数据格式，如果原本字符中含符号'['或']',则使用转义符'\'。
 *            2、通配符目前支持的格式如下
 *                [YYYYMMDD] 获取当前时间，如 20200411
 *                [GLR]      获取当前报表的管理人编号
 *                [REPORTID] 获取当前的报表代码
 *            3、默认是[GLR]/[REPORTID]/[YYYYMMDD]的动态路径
 *
 */
public class ParseWildcardChar {
  public static void main(String[] args) {
    String str="[GLR]/[REPORTID]/[YYYYMMDD]\\[";
    //[GLR]处理
    str=str.replace("[GLR]","1");
    //[REPORTID]处理
    str=str.replace("[REPORTID]","1231214");
    //[YYYYMMDD]处理
    str=str.replace("[YYYYMMDD]","20200101");

    System.out.println(str);
  }
}
