package com.legec.imgsearch.app;

import android.app.Activity;

import com.legec.imgsearch.app.activity.MenuActivity_;

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
        try {
            Thread.sleep(1000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MenuActivity_.intent(this).start();
    }
}
