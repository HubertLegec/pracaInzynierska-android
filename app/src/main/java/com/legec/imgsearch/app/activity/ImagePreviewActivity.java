package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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
        Glide.with(this)
                .load(imageUri)
                .fitCenter()
                .into(imgView);
    }

    @Click(R.id.back)
    void backClicked() {
        super.onBackPressed();
    }

    @Click(R.id.submit)
    void submitClick() {
        ResultActivity_.intent(this).start();
    }

}
