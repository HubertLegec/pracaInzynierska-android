package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;
import com.legec.imgsearch.app.result.DetailsDialogCallback;
import com.legec.imgsearch.app.result.ResultListAdapter;
import com.legec.imgsearch.app.result.ResultService;
import com.legec.imgsearch.app.result.ResultDetailsDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EActivity(R.layout.activity_result)
public class ResultActivity extends ListActivity {
    @ViewById
    ListView list;
    @Bean
    ResultListAdapter resultListAdapter;
    @Bean
    ResultService resultService;

    @AfterViews
    void bindAdapter() {
        setListAdapter(resultListAdapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sendImage();
        final Activity activity = this;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageDetails details = resultListAdapter.getItem(position);
                DetailsDialogCallback callback = new DetailsDialogCallback() {
                    @Override
                    public void onDialogClose(int result) {
                        if (result == ResultDetailsDialog.SHOW_BUTTON) {
                            //TODO
                        }
                    }
                };
                ResultDetailsDialog detailsDialog = new ResultDetailsDialog(details, activity, callback);
                detailsDialog.show();
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
