package com.legec.imgsearch.app.restConnection.dto;

import java.util.List;


public class SearchResponse {
    List<ImageDetails> images;

    public List<ImageDetails> getImages() {
        return images;
    }

    public void setImages(List<ImageDetails> images) {
        this.images = images;
    }
}
