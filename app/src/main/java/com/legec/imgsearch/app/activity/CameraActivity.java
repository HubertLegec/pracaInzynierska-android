package com.legec.imgsearch.app.activity;

/**
 * Created by hubert.legec on 2016-03-20.
 */
import android.app.Activity;
import android.os.Bundle;

import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;

@EActivity(R.layout.activity_camera)
public class CameraActivity extends Activity {

    @AfterViews
    public void initViews(){

    }

}