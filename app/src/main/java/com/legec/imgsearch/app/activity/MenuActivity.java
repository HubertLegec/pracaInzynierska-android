package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.utils.ErrorDialog;
import com.legec.imgsearch.app.utils.ImageSaver;
import com.legec.imgsearch.app.utils.PermissionManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.io.IOException;


@EActivity(R.layout.activity_menu)
public class MenuActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_TAKE_PICTURE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final String DIALOG = "dialog";

    @Bean
    PermissionManager permissionManager;
    @Bean
    ImageSaver imageSaver;

    private Uri photoUri;

    @Click(R.id.cameraButton)
    public void onCameraButtonClick() {
        boolean result = permissionManager.checkPermission(
                PermissionManager.CAMERA_PERMISSION,
                REQUEST_CAMERA_PERMISSION
        );
        if (result) {
            startCameraForResult();
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

    private void startCameraForResult() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = imageSaver.createImageFile();
                photoUri = FileProvider.getUriForFile(this, "com.legec.imgsearch.app.FileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, RESULT_TAKE_PICTURE);
            } catch (IOException e) {
                Toast.makeText(this, "Error occurred during camera start.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            imageSaver.setGalleryImage(selectedImage);
            openImagePreview(selectedImage);
        } else if (requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK) {
            imageSaver.galleryAddPic();
            openImagePreview(photoUri);
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void openImagePreview(Uri selectedImage) {
        ImagePreviewActivity_.intent(this)
                .extra("imageUri", selectedImage)
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showErrorDialog(R.string.camera_message);
            } else {
                startCameraForResult();
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION) {
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
        ErrorDialog dialog = new ErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ErrorDialog.ARG_MESSAGE, getString(resourceId));
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), DIALOG);
    }
}
