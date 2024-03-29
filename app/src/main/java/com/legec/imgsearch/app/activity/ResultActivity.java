package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;
import com.legec.imgsearch.app.result.DetailsDialogCallback;
import com.legec.imgsearch.app.result.ResultDetailsDialog;
import com.legec.imgsearch.app.result.ResultListAdapter;
import com.legec.imgsearch.app.result.ResultService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


@EActivity(R.layout.activity_result)
public class ResultActivity extends ListActivity {
    private static final String TAG = "ResultActivity";
    @ViewById
    ListView list;
    @ViewById
    ProgressBar progress;
    @ViewById
    LinearLayout listContainer;
    @Bean
    ResultListAdapter resultListAdapter;
    @Bean
    ResultService resultService;
    @ViewById
    LinearLayout empty;

    @AfterViews
    void bindAdapter() {
        setListAdapter(resultListAdapter);
        list.setEmptyView(empty);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sendImage();
        final Activity activity = this;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageDetails details = resultListAdapter.getItem(position);
                DetailsDialogCallback callback = new DetailsDialogCallback() {
                    @Override
                    public void onDialogClose(int result) {
                        if (result == ResultDetailsDialog.SHOW_BUTTON) {
                            openWebPage(details.getPageUrl());
                        }
                    }
                };
                ResultDetailsDialog detailsDialog = new ResultDetailsDialog(details, activity, callback);
                detailsDialog.show();
            }
        });
    }

    @Background
    void sendImage() {
        try {
            long startTime = System.nanoTime();
            List<ImageDetails> result = resultService.sendSearchRequest();
            long endTime = System.nanoTime();
            long time = (endTime - startTime) / 1000000;
            Log.d(TAG, "search request time: " + time + " ms");
            resultListAdapter.clear();
            resultListAdapter.addAll(result);
            refreshView();
        } catch (Exception e) {
            refreshView();
            showError(e.getMessage());
        }
    }

    @UiThread
    void refreshView(){
        progress.setVisibility(View.GONE);
        listContainer.setVisibility(View.VISIBLE);
        resultListAdapter.notifyDataSetChanged();
    }

    @UiThread
    void showError(String message) {
        Toast.makeText(this, "Connection error: " + message, Toast.LENGTH_LONG).show();
    }

    @Click(R.id.tryAgain)
    void onTryAgainClick() {
        listContainer.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        sendImage();
    }

    private void openWebPage(String url) {
        if (StringUtils.isEmpty(url)) {
            Toast.makeText(this, "Web page address is not available for this image", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
