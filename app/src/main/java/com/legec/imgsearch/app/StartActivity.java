package com.legec.imgsearch.app;

import android.app.Activity;
import android.content.Intent;

import com.legec.imgsearch.app.activity.CameraActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_start)
public class StartActivity extends Activity {

    @AfterViews
    void initViews() {
        waitMethod();
    }

    @Background
    void waitMethod() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000, 0);
                } catch (InterruptedException e) {

                }
            }
        });
        t.run();
        Intent intent = new Intent(this, CameraActivity_.class);
        startActivity(intent);
    }
}
