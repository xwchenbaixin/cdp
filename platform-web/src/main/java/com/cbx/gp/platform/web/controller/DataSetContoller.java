package com.cbx.gp.platform.web.controller;

import com.cbx.gp.platform.pojo.entity.CdpFile;
import com.cbx.gp.platform.pojo.entity.CdpUser;
import com.cbx.gp.platform.service.interfaces.DataCollectService;
import com.cbx.gp.platform.service.interfaces.FileDownloadService;
import com.cbx.gp.platform.service.interfaces.WebmagicService;
import org.apache.dubbo.config.annotation.Reference;
import com.cbx.gp.platform.pojo.entity.CdpDataSetDef;
import com.cbx.gp.platform.pojo.reqModel.RequestModel;
import com.cbx.gp.platform.pojo.resModel.ResponseModel;
import com.cbx.gp.platform.service.interfaces.DataSetService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Classname DataSetContoller
 * @Description TODO
 * @Date 2020/5/12 21:39
 * @Created by CBX
 */
@RestController
@RequestMapping("/dataSet")
public class DataSetContoller {
  @Reference
  private DataSetService dataSetService;

  @Reference(retries = -1,timeout = 30000)
  private WebmagicService webmagicService;

  @Reference
  private FileDownloadService fileDownloadService;

  private String path="z://cdpfile/";



  @RequestMapping("/listDataSet")
  public ResponseModel<CdpDataSetDef> listDataSet(@RequestBody RequestModel<CdpDataSetDef> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataSetService.listDataSet(req);
  }

  @RequestMapping("/getDataSetById")
  public ResponseModel<CdpDataSetDef> getDataSetById(@RequestBody RequestModel<CdpDataSetDef> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataSetService.selectByPrimaryKey(req.getParam().getId());
  }

  @RequestMapping("/saveScript")
  public ResponseModel<CdpDataSetDef> saveScript(@RequestBody RequestModel<CdpDataSetDef> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    return dataSetService.saveScript(req);
  }

  @RequestMapping("/exportExcel")
  public ResponseModel<CdpFile> exportExcel(@RequestBody RequestModel<CdpDataSetDef> req){
    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    CdpUser user= (CdpUser) request.getSession().getAttribute("user");
    req.setUser(user);
    ResponseModel<CdpFile> res=new ResponseModel<>();
    //获取数据仓库的数据
    List<Map<String, String>> resultData = webmagicService.getResultData(req.getParam().getTableName(), "");

    try {
      //根据数据集大小创建工作簿大小，行
      int size=resultData.size();
      if(size==0){
        size++;
      }
      SXSSFWorkbook wb = new SXSSFWorkbook(size);
      Sheet sh = wb.createSheet();
      wb.setCompressTempFiles(true);
      LinkedList<String> tableHeaders = new LinkedList<>();
      if(resultData.size()>0) {
        //设置表头
        for (Map.Entry<String, String> entry : resultData.get(0).entrySet()) {
          tableHeaders.add(entry.getKey());
        }
        Row row0 = sh.createRow(0);
        for (int i = 0; i < tableHeaders.size(); i++) {
          Cell cell = row0.createCell(i);
          cell.setCellValue(tableHeaders.get(i));
        }
      }
      // 使用createRow将信息写在内存中。
      // 其中0行为表头行
      for(int i=0;i<resultData.size();i++){
        int rownum=i+1;
        Row row=sh.createRow(rownum);
        Map<String,String> m=resultData.get(i);
        for(int cellnum=0;cellnum<tableHeaders.size();cellnum++){
          String val=m.get(tableHeaders.get(cellnum));
          Cell cell = row.createCell(cellnum);
          cell.setCellValue(val);
        }
      }
      String serialNo=null;
      String filePath=null;
      //判断该表的文件是否存在
      //拼接路径
      CdpFile fileInfo=fileDownloadService.getFileInfoByTableName(req.getParam().getTableName());
      InetAddress address = InetAddress.getLocalHost();
      String ip=address.getHostAddress();
      if(fileInfo==null){
        fileInfo=new CdpFile();
        serialNo= UUID.randomUUID().toString();
        filePath=this.path+serialNo+".xlsx";
        fileInfo.setFileName(req.getParam().getName()+".xlsx");
        fileInfo.setFilePath(filePath);
        fileInfo.setFileType("xlsx");
        fileInfo.setSerialNo(serialNo);
        fileInfo.setDownloadUrl("http://"+ip+":8081/download?serialNo="+serialNo);
        fileInfo.setTableName(req.getParam().getTableName());
        fileDownloadService.insertFileData(fileInfo);
      } else{
        serialNo=fileInfo.getSerialNo();
        filePath=fileInfo.getFilePath();
        fileInfo.setDownloadUrl("http://"+ip+":8081/download?serialNo="+serialNo);
        fileDownloadService.update(fileInfo);
      }
      File file=new File(filePath);
      if (file.exists()){
        file.delete();
      }
      FileOutputStream out = new FileOutputStream(filePath);
      wb.write(out);
      wb.close();
      out.close();

      //返回信息

      fileInfo.setFilePath(null);
      res.setData(fileInfo);
      res.setStatus(200);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return res;
  }
}
