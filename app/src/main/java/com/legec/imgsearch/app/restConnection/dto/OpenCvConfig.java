package com.legec.imgsearch.app.restConnection.dto;

import org.apache.commons.lang3.StringUtils;


public class OpenCvConfig {
    private String matcher_type;
    private int norm_type;
    private String extractor;

    public OpenCvConfig() {
    }

    public OpenCvConfig(String matcher_type, int norm_type, String extractor) {
        this.matcher_type = matcher_type;
        this.norm_type = norm_type;
        this.extractor = extractor;
    }

    public String getExtractor() {
        return extractor;
    }

    public void setExtractor(String extractor) {
        this.extractor = extractor;
    }

    public String getMatcher_type() {
        return matcher_type;
    }

    public void setMatcher_type(String matcher_type) {
        this.matcher_type = matcher_type;
    }

    public int getNorm_type() {
        return norm_type;
    }

    public void setNorm_type(int norm_type) {
        this.norm_type = norm_type;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(matcher_type) && StringUtils.isNotBlank(extractor);
    }
}
