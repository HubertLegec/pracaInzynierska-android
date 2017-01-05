package com.legec.imgsearch.app.restConnection.dto;


/**
 * Class represents search result list element
 * It contains url to image and match with query image ratio
 */
public class ImageDetails {
    private Double matchRate;
    private String url;
    private String name;
    private String pageUrl;

    public Double getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(Double matchRate) {
        this.matchRate = matchRate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
