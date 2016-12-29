package com.legec.imgsearch.app.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private String currentPhotoPath;
    private String filename;
    private Uri galleryFileUri;

    public File createImageFile() throws IOException {
        galleryFileUri = null;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        filename = image.getName();
        return image;
    }

    public void galleryAddPic() {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), currentPhotoPath, filename, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setGalleryImage(Uri uri) {
        filename = null;
        this.galleryFileUri = uri;
    }

    /**
     * Returns stored camera image
     * @return image as {@link ByteArrayResource} or null if value is not present
     */
    public ByteArrayResource getImage() {
        Bitmap img = null;
        if(filename != null) {
            img = BitmapFactory.decodeFile(currentPhotoPath);
        } else {
            try {
                img = MediaStore.Images.Media.getBitmap(context.getContentResolver(), galleryFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream os = resizeAndCompressImage(img);
        byte[] bytes = os.toByteArray();
        return new ImageResource(bytes, TEMP_FILE_NAME);
    }

    /**
     * Resize and compress image. Shorter edge is set to {@link #IMG_SHORTER_EDGE_SIZE} and longer is proportional to it.
     * @param img Input image to process
     * @return Processed image as {@link ByteArrayOutputStream}
     */
    private static ByteArrayOutputStream resizeAndCompressImage(Bitmap img) {
        Log.i(TAG, "resize and compress image");
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