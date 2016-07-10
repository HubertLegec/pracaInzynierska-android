package com.legec.imgsearch.app.result;


public class ResultEntry {
    public long id;
    public String name = "Name:";

    public ResultEntry() {
    }

    public ResultEntry(long id, String name) {
        this.id = id;
        this.name = name;
    }
}