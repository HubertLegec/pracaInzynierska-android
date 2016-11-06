package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.RestClient;
import com.legec.imgsearch.app.settings.CommonSettings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;


@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {
    private CommonSettings settings;
    @ViewById
    EditText serverAddress;

    @ViewById
    Button testButton;

    @RestService
    RestClient restClient;

    @AfterViews
    void initViews(){
        settings = new CommonSettings(this);
        serverAddress.setText(settings.getServerAddres());
    }

    @Click(R.id.testButton)
    void onTestButtonClick() {
        updateServerAddress();
        checkServerStatus();
    }

    @Override
    protected void onPause() {
        updateServerAddress();
        super.onPause();
    }

    private void updateServerAddress() {
        String newAddress = serverAddress.getText().toString();
        if (!settings.getServerAddres().equals(newAddress)) {
            restClient.setRootUrl("http://" + newAddress);
            settings.setServerAddress(newAddress);
        }
    }

    @Background
    void checkServerStatus() {
        try {
            String response = restClient.healthCheck().getBody();
            if ("OK".equals(response)) {
                updateButtonColor(Color.GREEN);
            } else {
                updateButtonColor(Color.RED);
            }
        } catch (Exception e) {
            updateButtonColor(Color.RED);
        }
    }

    @UiThread
    void updateButtonColor(int color) {
        testButton.setBackgroundColor(color);
    }
}
