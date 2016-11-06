package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ListView;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.RestClient;
import com.legec.imgsearch.app.result.ResultListAdapter;
import com.legec.imgsearch.app.utils.ImageSaver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.ByteArrayOutputStream;


@EActivity(R.layout.activity_result)
public class ResultActivity extends Activity {
    @ViewById
    ListView resultList;

    @RestService
    RestClient restClient;

    @Bean
    ResultListAdapter resultListAdapter;

    @AfterViews
    void bindAdapter() {
        resultList.setAdapter(resultListAdapter);
        sendImage();
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
            newHeight = 500;
            newWidth = (int)((float)newHeight / ratio);
        }
        else {
            newWidth = 500;
            newHeight = (int)((float)newWidth * ratio);
        }

        Bitmap scaledImg = Bitmap.createScaledBitmap(img, newWidth, newHeight, false);

        ByteArrayOutputStream imgByteStream = new ByteArrayOutputStream();
        scaledImg.compress(Bitmap.CompressFormat.JPEG, 75, imgByteStream);

        //ResponseEntity<SearchResponse> result = restClient.search(imgByteStream.toByteArray());

        //List<ImageDetails> resultImages = result.getBody().getMatchingImages();

        resultListAdapter.clear();
        //for(ImageDetails imgDet : resultImages){
        //    resultListAdapter.addItem(new ResultEntry(imgDet.getId(), "ID:"));
        //}
        refreshView();
    }

    @UiThread
    void refreshView(){
        resultListAdapter.notifyDataSetChanged();
    }
}
