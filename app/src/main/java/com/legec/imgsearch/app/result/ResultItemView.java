package com.legec.imgsearch.app.result;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.result_element)
public class ResultItemView extends LinearLayout {
    @ViewById
    public TextView matchRatio;
    @ViewById
    public TextView name;
    @ViewById
    public ImageView imageView;


    public ResultItemView(Context context) {
        super(context);
    }

    public void bind(ImageDetails entry) {
        name.setText("Name: " + entry.getName());
        matchRatio.setText("Match rate: " + String.format("%.3f", entry.getMatchRate()));
    }
}
