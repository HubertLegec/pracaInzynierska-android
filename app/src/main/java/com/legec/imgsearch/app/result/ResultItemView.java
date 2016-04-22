package com.legec.imgsearch.app.result;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.Semaphore;


@EViewGroup(R.layout.result_element)
public class ResultItemView extends LinearLayout {

    @ViewById
    public TextView name;
    @ViewById
    public ImageView image;
    @ViewById
    public TextView description;


    public ResultItemView(Context context) {
        super(context);
    }

    public void bind(ResultEntry person, Bitmap imageBitmap, Semaphore semaphore) {
        name.setText(person.name);
        description.setText(person.description);
        try {
            semaphore.acquire();
            Bitmap bitmap = imageBitmap;
            semaphore.release();
            image.setImageBitmap(bitmap);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
