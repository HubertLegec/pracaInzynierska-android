package com.legec.imgsearch.app.utils;

import com.legec.imgsearch.app.restConnection.RestClient;

import org.springframework.core.io.ByteArrayResource;


/**
 * Extends {@link ByteArrayResource} to implement {@link #getFilename()} method.
 * It is required by {@link RestClient} to send multipart request.
 */
class ImageResource extends ByteArrayResource {

    private String filename;

    ImageResource(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}
