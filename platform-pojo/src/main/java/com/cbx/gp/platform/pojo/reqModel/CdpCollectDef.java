package com.cbx.gp.platform.pojo.reqModel;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cbx.gp.platform.pojo.entity.ParamRule;

import java.io.Serializable;
import java.util.List;

public class CdpCollectDef implements Serializable {

    private Integer id;


    private String collectName;

    private String dataDomain;

    private Long userId;

    private String nextPage;

    private Integer nextPageType;

    private String collectUrl;

    private Integer nextPageTotal;

    private List<ParamRule> collectParam;

    private String headers;

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

    public String getDataDomain() {
        return dataDomain;
    }

    public void setDataDomain(String dataDomain) {
        this.dataDomain = dataDomain == null ? null : dataDomain.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getCollectParam() {
        return JSON.toJSONString(collectParam);
    }

    public void setCollectParam(String collectParam) {
        System.out.println(collectParam);
        this.collectParam=null;//重置m
        if(collectParam!=null&&!collectParam.equals("")){
            this.collectParam = JSONObject.parseArray(collectParam, ParamRule.class);
        }
    }

    public List<ParamRule> getCollectParamList(){
        return collectParam;
    }

    @Override
    public String toString() {
        return "CdpCollectDef{" +
                "id=" + id +
                ", collectName='" + collectName + '\'' +
                ", dataDomain='" + dataDomain + '\'' +
                ", userId=" + userId +
                ", nextPage='" + nextPage + '\'' +
                ", nextPageType=" + nextPageType +
                ", collectUrl='" + collectUrl + '\'' +
                ", nextPageTotal=" + nextPageTotal +
                ", collectParam=" + collectParam +
                ", headers='" + headers + '\'' +
                '}';
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }
}