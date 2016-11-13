package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.widget.ListView;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.ConnectionService;
import com.legec.imgsearch.app.result.ResultListAdapter;
import com.legec.imgsearch.app.utils.ImageSaver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;


@EActivity(R.layout.activity_result)
public class ResultActivity extends Activity {
    @ViewById
    ListView resultList;

    @Bean
    ConnectionService connectionService;
    @Bean
    ResultListAdapter resultListAdapter;
    @Bean
    ImageSaver imageSaver;

    @AfterViews
    void bindAdapter() {
        resultList.setAdapter(resultListAdapter);
        sendImage();
    }

    @Background(id = "sendTask")
    void sendImage() {
        ByteArrayResource image = imageSaver.getImage();


        List<String> result = connectionService.findByImage(image);

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
