package com.legec.imgsearch.app.result;


public class ResultEntry {
    public int id;
    public String name = "Name:";

    public ResultEntry() {
    }

    public ResultEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }
}