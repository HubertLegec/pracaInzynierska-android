package com.legec.imgsearch.app.restConnection;

import java.util.ArrayList;
import java.util.List;


public class SearchResponse {
    private List<ImageDetails> matchingImages = new ArrayList<>();

    public SearchResponse() {
    }

    public SearchResponse(List<ImageDetails> matchingImages) {
        this.matchingImages = matchingImages;
    }

    public List<ImageDetails> getMatchingImages() {
        return matchingImages;
    }

    public void setMatchingImages(List<ImageDetails> matchingImages) {
        this.matchingImages = matchingImages;
    }
}
