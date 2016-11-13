package com.legec.imgsearch.app.utils;

import org.springframework.core.io.ByteArrayResource;


public class ImageResource extends ByteArrayResource {

    private String filename;

    public ImageResource(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}
