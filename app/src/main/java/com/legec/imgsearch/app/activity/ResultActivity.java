package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.pastec.PastecRestClient;
import com.legec.imgsearch.app.restConnection.pastec.SearchResponse;
import com.legec.imgsearch.app.result.ResultEntry;
import com.legec.imgsearch.app.result.ResultListAdapter;
import com.legec.imgsearch.app.utils.ImageSaver;
import com.legec.imgsearch.app.utils.SearchResultStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

@EActivity(R.layout.activity_result)
public class ResultActivity extends Activity {
    @ViewById
    LinearLayout noResultLayout;
    @ViewById
    LinearLayout resultLayout;
    @ViewById
    ImageButton rebeginButton;
    @ViewById
    ImageButton backButton;
    @ViewById
    ImageButton noGoodResultsButton;
    @ViewById
    TextView errorTextView;
    @ViewById
    ListView resultList;

    private ProgressDialog mProgressDialog;

    @RestService
    PastecRestClient pastecRestClient;

    @Bean
    ResultListAdapter resultListAdapter;

    @AfterViews
    void bindAdapter() {
        resultList.setAdapter(resultListAdapter);
        sendImage();
    }


    @ItemClick
    void resultListItemClicked(ResultEntry entry) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(entry.urlPage));
        startActivity(i);
    }

    @Click(R.id.rebeginButton)
    void rebeginButtonClick() {
        finish();
    }

    @Click(R.id.backButton)
    void backButtonClick() {
        ResultActivity.this.finish();
    }

    @Click(R.id.noGoodResultsButton)
    void noGoodResultsButtonClick() {
        //Util.informNoGoodResults(mReqId);
        ResultActivity.this.finish();
    }

    @Background(id = "sendTask")
    void sendImage() {
        byte[] imageBytes = ImageSaver.getImage();
        Bitmap img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        int height = img.getHeight();
        int width = img.getWidth();
        float ratio = (float)height/(float)width;
        int newWidth;
        int newHeight;
        if (height > width) {
            newWidth = 500;
            newHeight = (int)((float)newWidth / ratio);
        }
        else {
            newHeight = 500;
            newWidth = (int)((float)newHeight * ratio);
        }

        Bitmap scaledImg = Bitmap.createScaledBitmap(img, newWidth, newHeight, false);

        ByteArrayOutputStream imgByteStream = new ByteArrayOutputStream();
        scaledImg.compress(Bitmap.CompressFormat.JPEG, 75, imgByteStream);

        ResponseEntity<SearchResponse> result = pastecRestClient.search(imgByteStream);

    }

    private final Handler mHandler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SearchResultStatus.RESULT_RECEIVED:
                    resultListAdapter.getImages();
                    resultListAdapter.notifyDataSetChanged();
                    noResultLayout.setVisibility(View.GONE);
                    resultLayout.setVisibility(View.VISIBLE);
                    break;
                case SearchResultStatus.NO_RESULT:
                    noResultLayout.setVisibility(View.VISIBLE);
                    resultLayout.setVisibility(View.GONE);
                    errorTextView.setText(ResultActivity.this.getText(R.string.no_result));
                    break;
                case SearchResultStatus.SERVICE_DOWN:
                    noResultLayout.setVisibility(View.VISIBLE);
                    resultLayout.setVisibility(View.GONE);
                    errorTextView.setText(ResultActivity.this.getText(R.string.service_down));
                    break;
                case SearchResultStatus.SHOW_WAITING_DIALOG:
                    mProgressDialog = new ProgressDialog(ResultActivity.this, R.style.ProgressDialog);
                    mProgressDialog.setMessage(ResultActivity.this.getText(R.string.retrieving));
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.setOnCancelListener(new MyCancelListener());
                    mProgressDialog.show();
                    break;
                case SearchResultStatus.HIDE_WAITING_DIALOG:
                    mProgressDialog.setOnCancelListener(null);
                    mProgressDialog.cancel();
                    break;
            }
        }
    }


    private class MyCancelListener implements ProgressDialog.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialog) {
            BackgroundExecutor.cancelAll("sendTask", true);
            finish();
        }
    }
}
