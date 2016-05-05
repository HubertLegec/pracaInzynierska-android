package com.legec.imgsearch.app.restConnection;

public class InsertImageResult {
    private String result;
    private Integer id;

    public InsertImageResult() {
    }

    public InsertImageResult(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}