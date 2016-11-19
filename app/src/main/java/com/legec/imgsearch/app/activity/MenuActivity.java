package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.utils.ErrorDialog;
import com.legec.imgsearch.app.utils.PermissionManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_menu)
public class MenuActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final String DIALOG = "dialog";

    @Bean
    PermissionManager permissionManager;

    @AfterViews
    public void initViews() {

    }

    @Click(R.id.cameraButton)
    public void onCameraButtonClick() {
        boolean result = permissionManager.checkPermission(
                PermissionManager.CAMERA_PERMISSION,
                REQUEST_CAMERA_PERMISSION
        );
        if (result) {
            CameraActivity_.intent(this).start();
        }
    }

    @Click(R.id.galleryButton)
    public void onGalleryButtonClick() {
        boolean result = permissionManager.checkPermission(
                PermissionManager.EXTERNAL_STORAGE_PERMISSION,
                REQUEST_EXTERNAL_STORAGE_PERMISSION
        );
        if (result) {
            startGalleryForResult();
        }
    }

    @Click(R.id.settingsButton)
    public void onSettingsButtonClick() {
        SettingsActivity_.intent(this).start();
    }

    private void startGalleryForResult() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            ImagePreviewActivity_.intent(this).extra("imageUri", selectedImage).start();
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showErrorDialog(R.string.camera_message);
            } else {
                CameraActivity_.intent(this).start();
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION){
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showErrorDialog(R.string.external_storage_message);
            } else {
                startGalleryForResult();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showErrorDialog(int resourceId) {
        ErrorDialog.newInstance(getString(resourceId))
                .show(getFragmentManager(), DIALOG);
    }
}
