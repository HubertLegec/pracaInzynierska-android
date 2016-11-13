package com.legec.imgsearch.app.restConnection.dto;


import java.util.List;

public class Vocabulary {
    private List<List<Float>> vocabulary;

    public List<List<Float>> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<List<Float>> vocabulary) {
        this.vocabulary = vocabulary;
    }
}
