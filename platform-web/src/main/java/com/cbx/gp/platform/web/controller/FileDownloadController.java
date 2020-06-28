package com.cbx.gp.platform.web.controller;

import com.cbx.gp.platform.pojo.entity.CdpFile;
import com.cbx.gp.platform.service.interfaces.FileDownloadService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Classname FileDownloadController
 * @Description TODO
 * @Date 2020/6/2 21:10
 * @Created by CBX
 */
@RestController
@RequestMapping("/")
public class FileDownloadController {
  @Reference
  private FileDownloadService fileDownloadService;

  @RequestMapping("/download")
  public void downloadExcel(String serialNo,HttpServletRequest request, HttpServletResponse response){
    CdpFile cf=fileDownloadService.getFileInfo(serialNo);
    File file = new File(cf.getFilePath());
    byte[] buffer = new byte[1024];
    BufferedInputStream bis = null;
    OutputStream os = null; //输出流
    try {
      //判断文件父目录是否存在
      if (file.exists()) {
        //设置返回文件信息
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(cf.getFileName(),"UTF-8"));
        response.addHeader("Content-Length", "" + file.length());
        os = response.getOutputStream();
        bis = new BufferedInputStream(new FileInputStream(file));
        while(bis.read(buffer) != -1){
          os.write(buffer);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if(bis != null) {
          bis.close();
        }
        if(os != null) {
          os.flush();
          os.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
