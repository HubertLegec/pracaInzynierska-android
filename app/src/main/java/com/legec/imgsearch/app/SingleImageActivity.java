package com.legec.imgsearch.app;

/**
 * Created by hubert.legec on 2016-03-20.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EActivity;

import java.io.File;

public class SingleImageActivity extends Activity {

    private static final String IMAGE_FILE_LOCATION = "image_file_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width, height
        );
        imageView.setLayoutParams(params);

        setContentView(imageView);

        File imageFile = new File(
                getIntent().getStringExtra(IMAGE_FILE_LOCATION)
        );

        SingleImageBitmapWorkerTask workerTask = new SingleImageBitmapWorkerTask(imageView, width, height);
        workerTask.execute(imageFile);

    }
}
