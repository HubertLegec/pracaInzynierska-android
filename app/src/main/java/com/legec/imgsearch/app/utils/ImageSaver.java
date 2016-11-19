package com.legec.imgsearch.app.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * There is only one instance of this class in the application, it's a singleton.
 * Saves a JPEG {@link Image} into {@link ByteArrayResource} object.
 * Saved file is then send to server.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ImageSaver {
    private static final String TAG = "IMAGE_SAVER";
    private static final String TEMP_FILE_NAME = "queryImage.jpg";
    private static final int IMG_SHORTER_EDGE_SIZE = 500;

    private ByteArrayResource imageResource;

    public void setImage(Image image) {
        Log.i(TAG, "set image");
        ByteArrayOutputStream os = resizeAndCompressImage(image);
        byte[] bytes = os.toByteArray();
        imageResource = new ImageResource(bytes, TEMP_FILE_NAME);
        Log.i(TAG, "set image successful");
    }

    /**
     * Returns stored camera image
     * @return image as {@link ByteArrayResource} or null if value is not present
     */
    public ByteArrayResource getImage() {
        return imageResource;
    }

    /**
     * Resizes and compresses image. Shorter edge is set to {@link #IMG_SHORTER_EDGE_SIZE} and longer is proportional to it.
     * @param image Input image to process
     * @return Processed image as {@link ByteArrayOutputStream}
     */
    private ByteArrayOutputStream resizeAndCompressImage(Image image) {
        Log.i(TAG, "resize and compress image");
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] imageBytes = new byte[buffer.remaining()];
        buffer.get(imageBytes);
        Bitmap img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        int height = img.getHeight();
        int width = img.getWidth();
        float ratio = (float) height / (float) width;
        if (height < width) {
            height = IMG_SHORTER_EDGE_SIZE;
            width = (int) ((float) height / ratio);
        } else {
            width = IMG_SHORTER_EDGE_SIZE;
            height = (int) ((float) width * ratio);
        }
        Bitmap scaledImg = Bitmap.createScaledBitmap(img, width, height, false);
        ByteArrayOutputStream imgByteStream = new ByteArrayOutputStream();
        scaledImg.compress(Bitmap.CompressFormat.JPEG, 75, imgByteStream);
        Log.i(TAG, "image resize and compress successful");
        return imgByteStream;
    }

}