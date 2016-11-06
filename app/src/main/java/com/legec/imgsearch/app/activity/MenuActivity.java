package com.legec.imgsearch.app.activity;

import android.app.Activity;

import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_menu)
public class MenuActivity extends Activity {

    @AfterViews
    public void initViews(){

    }

    @Click(R.id.cameraButton)
    public void onCameraButtonClick(){
        CameraActivity_.intent(this).start();
    }

    @Click(R.id.galleryButton)
    public void onGalleryButtonClick(){

    }

    @Click(R.id.settingsButton)
    public void onSettingsButtonClick(){
        SettingsActivity_.intent(this).start();
    }
}
