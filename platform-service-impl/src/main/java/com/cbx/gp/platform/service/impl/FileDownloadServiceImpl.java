package com.cbx.gp.platform.service.impl;

import com.cbx.gp.platform.dao.mapper.CdpFileMapper;
import com.cbx.gp.platform.pojo.entity.CdpFile;
import com.cbx.gp.platform.pojo.entity.CdpFileExample;
import com.cbx.gp.platform.service.interfaces.FileDownloadService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname FileDownloadServiceImpl
 * @Description TODO
 * @Date 2020/6/2 21:49
 * @Created by CBX
 */
@Service
public class FileDownloadServiceImpl implements FileDownloadService {
  @Autowired
  private CdpFileMapper cdpFileMapper;

  @Override
  public Boolean insertFileData(CdpFile cf){
    if(cdpFileMapper.insertSelective(cf)==1){
      return true;
    }
    else{
      return false;
    }
  }

  @Override
  public CdpFile getFileInfo(String serialNo){
    return cdpFileMapper.selectByPrimaryKey(serialNo);
  }

  @Override
  public CdpFile getFileInfoByTableName(String tableName) {
    CdpFileExample cfe=new CdpFileExample();
    cfe.createCriteria().andTableNameEqualTo(tableName);
    List<CdpFile> data=cdpFileMapper.selectByExample(cfe);
    if(data.size()==0)
      return null;
    else
      return data.get(0);
  }

  @Override
  public int update(CdpFile fileInfo) {
    return cdpFileMapper.updateByPrimaryKeySelective(fileInfo);
  }


}
