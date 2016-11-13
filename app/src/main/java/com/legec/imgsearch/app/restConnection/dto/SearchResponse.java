package com.legec.imgsearch.app.restConnection.dto;


import java.util.List;

public class SearchResponse {
    List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
