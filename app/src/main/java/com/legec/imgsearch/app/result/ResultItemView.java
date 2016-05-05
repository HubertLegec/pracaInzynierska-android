package com.legec.imgsearch.app.result;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.result_element)
public class ResultItemView extends LinearLayout {

    @ViewById
    public TextView id;
    @ViewById
    public TextView name;


    public ResultItemView(Context context) {
        super(context);
    }

    public void bind(ResultEntry entry) {
        name.setText(entry.name);
        id.setText(String.valueOf(entry.id));
    }

}
