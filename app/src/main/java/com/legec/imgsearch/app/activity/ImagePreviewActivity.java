package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_preview)
public class ImagePreviewActivity extends Activity {

    @Extra("imageUri")
    Uri imageUri;
    @ViewById
    ImageView imgView;

    @AfterViews
    public void initViews() {
        loadImage();
    }

    void loadImage() {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        // Set the Image in ImageView after decoding the String
        imgView.setImageBitmap(BitmapFactory
                .decodeFile(imgDecodableString));
    }
}
