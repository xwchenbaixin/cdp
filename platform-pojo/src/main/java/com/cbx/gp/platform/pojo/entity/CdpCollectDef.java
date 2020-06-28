package com.cbx.gp.platform.pojo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author null
 * @date 2020/05/09
 */
public class CdpCollectDef implements Serializable {
    private Integer id;

    private String collectName;

    private String collectParam;

    private String headers;

    private String dataDomain;

    private Integer userId;

    private String nextPage;

    private Integer nextPageType;

    private String collectUrl;

    private Integer nextPageTotal;

    private Date operateTime;

    private Date createTime;

    private Integer dataSetDefId;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollectName() {
        return collectName;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName == null ? null : collectName.trim();
    }

    public String getCollectParam() {
        return collectParam;
    }

    public void setCollectParam(String collectParam) {
        this.collectParam = collectParam == null ? null : collectParam.trim();
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers == null ? null : headers.trim();
    }

    public String getDataDomain() {
        return dataDomain;
    }

    public void setDataDomain(String dataDomain) {
        this.dataDomain = dataDomain == null ? null : dataDomain.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage == null ? null : nextPage.trim();
    }

    public Integer getNextPageType() {
        return nextPageType;
    }

    public void setNextPageType(Integer nextPageType) {
        this.nextPageType = nextPageType;
    }

    public String getCollectUrl() {
        return collectUrl;
    }

    public void setCollectUrl(String collectUrl) {
        this.collectUrl = collectUrl == null ? null : collectUrl.trim();
    }

    public Integer getNextPageTotal() {
        return nextPageTotal;
    }

    public void setNextPageTotal(Integer nextPageTotal) {
        this.nextPageTotal = nextPageTotal;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDataSetDefId() {
        return dataSetDefId;
    }

    public void setDataSetDefId(Integer dataSetDefId) {
        this.dataSetDefId = dataSetDefId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}