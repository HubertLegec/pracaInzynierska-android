package com.legec.imgsearch.app.utils;

/**
 * Created by hubert.legec on 2016-04-03.
 */

import android.media.Image;
import java.io.File;
import java.nio.ByteBuffer;

/**
 * Saves a JPEG {@link Image} into the specified {@link File}.
 */
public class ImageSaver {

    /**
     * The JPEG image
     */
    private static byte[] bytes;

    public ImageSaver() {

    }

    public static void setImage(Image image){
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
    }

    public static byte[] getImage(){
        return bytes;
    }

}