package com.legec.imgsearch.app.result;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;


@EViewGroup(R.layout.result_element)
public class ResultItemView extends LinearLayout {
    @ViewById
    Button webPageButton;
    @ViewById
    TextView name;
    @ViewById
    ImageView imageView;
    @ViewById
    ProgressBar progress;

    private final Context context;


    public ResultItemView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(ImageDetails entry) {
        name.setText(entry.getName());
        final String url = entry.getPageUrl();
        if (StringUtils.isBlank(url)) {
            webPageButton.setEnabled(false);
        } else {
            webPageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }
    }
}
