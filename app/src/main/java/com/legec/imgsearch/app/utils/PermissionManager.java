package com.legec.imgsearch.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class PermissionManager {
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static final String EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    @RootContext
    Activity context;

    public boolean checkPermission(String permission, int requestId) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(permission, requestId);
            return false;
        }
        return true;
    }

    private void requestPermission(String permission, int requestId) {
        ActivityCompat.requestPermissions(context, new String[]{permission}, requestId);
    }
}
