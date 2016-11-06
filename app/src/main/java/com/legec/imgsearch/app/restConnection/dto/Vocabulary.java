package com.legec.imgsearch.app.restConnection.dto;


import java.util.List;

public class Vocabulary {
    private List<List<Double>> vocabulary;

    public List<List<Double>> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<List<Double>> vocabulary) {
        this.vocabulary = vocabulary;
    }
}
