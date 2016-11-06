package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.widget.EditText;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.RestClient;
import com.legec.imgsearch.app.settings.CommonSettings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {
    private CommonSettings settings;
    @ViewById
    EditText serverAddress;

    @RestService
    RestClient restClient;

    @AfterViews
    public void initViews(){
        settings = new CommonSettings(this);
        serverAddress.setText(settings.getServerAddres());
    }

    @Override
    protected void onPause() {
        String newAddress = serverAddress.getText().toString();
        if (!settings.getServerAddres().equals(newAddress)) {
            restClient.setRootUrl(newAddress);
            settings.setServerAddress(newAddress);
        }
    }
}
