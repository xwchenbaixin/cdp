package com.cbx.gp.platform.service.interfaces;

import com.cbx.gp.platform.pojo.entity.CdpFile;

/**
 * @Classname FileDownloadService
 * @Description TODO
 * @Date 2020/6/2 21:17
 * @Created by CBX
 */
public interface FileDownloadService {

  Boolean insertFileData(CdpFile cf);

  CdpFile getFileInfo(String serialNo);

  CdpFile getFileInfoByTableName(String tableName);

  int update(CdpFile fileInfo);
}
