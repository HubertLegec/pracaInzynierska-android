package com.legec.imgsearch.app;

/**
 * Created by hubert.legec on 2016-03-20.
 */
import android.app.Activity;
import android.os.Bundle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;

@EActivity(R.layout.activity_camera)
public class CameraActivity extends Activity {

    @InstanceState
    int someId;

    @InstanceState
    Bundle savedInstanceState;

    @AfterViews
    public void initViews(){
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }
    }

}