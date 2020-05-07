package com.cbx.gp.platform.pojo.entity;

/**
 * 
 * @author null
 * @date 2020/05/07
 */
public class CdpCollectDef {
    private Integer id;

    private String collectName;

    private String dataDomain;

    private Long userId;

    private String nextPage;

    private Integer nextPageType;

    private String collectUrl;

    private Integer nextPageTotal;

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
}