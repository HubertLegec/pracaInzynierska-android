package com.legec.imgsearch.app.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @RootContext
    Context context;

    private boolean writeExternalStorage = false;
    private ByteArrayResource imageResource;

    @AfterInject
    void afterDependenciesInject() {
        writeExternalStorage = (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public void setImage(Image image) {
        Log.i(TAG, "set image");
        ByteArrayOutputStream os = resizeAndCompressImage(image);
        byte[] bytes = os.toByteArray();
        imageResource = new ImageResource(bytes, TEMP_FILE_NAME);
        //saveFileOnDeviceStorage(bytes);
        Log.i(TAG, "set image successful");
    }

    /**
     * TODO - remove, it is used only for testing
     * @param bytes
     */
    private void saveFileOnDeviceStorage(byte[] bytes) {
        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "imgSearch");
            f.mkdirs();
            FileOutputStream str = new FileOutputStream(f.getAbsolutePath() + "/img1.jpg");
            str.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns stored camera image
     * @return image as {@link ByteArrayResource} or null if value is not present
     */
    public ByteArrayResource getImage() {
        return imageResource;
    }

    /**
     * Clears stored image, after this operation {@link #getImage()} returns null
     */
    public void removeTempFileIfPresent() {
        imageResource = null;
        Log.i(TAG, "remove temp file");
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

    //TODO - remove, used only for testing
    public void checkWritePermission(Activity activity) {
        int REQUEST_WRITE_STORAGE = 112;
        if (!writeExternalStorage) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

}