package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;
import com.legec.imgsearch.app.result.ResultListAdapter;
import com.legec.imgsearch.app.result.ResultService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EActivity(R.layout.activity_result)
public class ResultActivity extends Activity {
    @ViewById
    ListView resultList;
    @ViewById
    ImageView selectedImage;
    @Bean
    ResultListAdapter resultListAdapter;
    @Bean
    ResultService resultService;

    @AfterViews
    void bindAdapter() {
        resultList.setAdapter(resultListAdapter);
        resultList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sendImage();
        final Activity activity = this;

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultListAdapter.updateSelectedView(view);
                Glide.with(activity)
                        .load(resultListAdapter.getItem(position).getUrl())
                        .fitCenter()
                        .placeholder(R.drawable.progress_spinner)
                        .crossFade()
                        .into(selectedImage);
            }
        });
    }

    @Background(id = "sendTask")
    void sendImage() {
        try {
            List<ImageDetails> result = resultService.sendSearchRequest();
            resultListAdapter.clear();
            resultListAdapter.addAll(result);
            refreshView();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @UiThread
    void refreshView(){
        resultListAdapter.notifyDataSetChanged();
    }

    @UiThread
    void showError(String message) {
        Toast.makeText(this, "Connection error: " + message, Toast.LENGTH_LONG).show();
    }
}
