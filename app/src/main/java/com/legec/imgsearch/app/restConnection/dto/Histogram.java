package com.legec.imgsearch.app.restConnection.dto;


import java.util.List;

public class Histogram {
    private List<List<Float>> histogram;

    public List<List<Float>> getHistogram() {
        return histogram;
    }

    public void setHistogram(List<List<Float>> histogram) {
        this.histogram = histogram;
    }
}
