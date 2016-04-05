package com.legec.imgsearch.app.camera;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;

import com.legec.imgsearch.app.utils.CompareSizesByArea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hubert.legec on 2016-04-03.
 */
public class Camera {
    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /** Tag for the {@link Log}. */
    public static final String TAG = "CameraFragment";

    /** Camera state: Showing camera preview. */
    public static final int STATE_PREVIEW = 0;

    /** Camera state: Waiting for the focus to be locked. */
    public static final int STATE_WAITING_LOCK = 1;

    /** Camera state: Waiting for the exposure to be precapture state. */
    public static final int STATE_WAITING_PRECAPTURE = 2;

    /** Camera state: Waiting for the exposure state to be something other than precapture. */
    public static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /** Camera state: Picture was taken. */
    public static final int STATE_PICTURE_TAKEN = 4;

    /**
     * The current state of camera state for taking pictures.
     *
     * see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /** Whether the current camera device supports Flash or not. */
    private boolean mFlashSupported;

    /** ID of the current {@link CameraDevice}. */
    private String mCameraId;

    /** The {@link Size} of camera preview. */
    private Size mPreviewSize;


    public String getmCameraId() {
        return mCameraId;
    }

    public void setmCameraId(String mCameraId) {
        this.mCameraId = mCameraId;
    }

    public int getmState() {
        return mState;
    }

    public void setmState(int mState) {
        this.mState = mState;
    }

    public boolean ismFlashSupported() {
        return mFlashSupported;
    }

    public void setmFlashSupported(boolean mFlashSupported) {
        this.mFlashSupported = mFlashSupported;
    }

    public void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    public Size getmPreviewSize() {
        return mPreviewSize;
    }

    public void setmPreviewSize(Size mPreviewSize) {
        this.mPreviewSize = mPreviewSize;
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     */
    public void chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            mPreviewSize =  Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            mPreviewSize = Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            mPreviewSize = choices[0];
        }
    }


    /**
     * Configures the necessary {@link Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     * @param activity Current activity
     * @param mTextureView Texture view
     */
    public void getTransformMatrix(int viewHeight, int viewWidth, Activity activity, TextureView mTextureView){
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int previewWidth = mPreviewSize.getWidth();
        int previewHeight = mPreviewSize.getHeight();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewHeight, previewWidth);
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewHeight,
                    (float) viewWidth / previewWidth);
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }


}
