package com.legec.imgsearch.app.restConnection.dto;


public class Matcher {
    private String matcher_type;
    private int norm_type;

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
}
