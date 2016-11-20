package com.legec.imgsearch.app.restConnection.dto;


import java.util.List;

public class Vocabulary {
    private List<List<Float>> vocabulary;
    private int size;
    private int rowSize;

    public List<List<Float>> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<List<Float>> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }
}
