package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.opencv.OpenCvService;
import com.legec.imgsearch.app.restConnection.ConnectionService;
import com.legec.imgsearch.app.restConnection.callbacks.LoadMetadataCallback;
import com.legec.imgsearch.app.restConnection.callbacks.NoParamsCallback;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;
import com.legec.imgsearch.app.settings.GlobalSettings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;


@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {
    @ViewById
    EditText serverAddress;
    @ViewById
    Button testButton;
    @ViewById
    CheckBox loadedCheckbox;
    @ViewById
    Switch queryingSwitch;
    @Bean
    ConnectionService connectionService;
    @Bean
    GlobalSettings settings;
    @Bean
    OpenCvService openCvService;


    @AfterViews
    void initViews() {
        serverAddress.setText(settings.getServerAddress());
        loadedCheckbox.setChecked(settings.isMetadataLoaded());
        queryingSwitch.setChecked(settings.getQueryingMethod());
    }

    @Click(R.id.testButton)
    void onTestButtonClick() {
        updateServerAddress();
        connectionService.checkServerStatus(new NoParamsCallback() {
            @Override
            public void onSuccess() {
                updateButtonColor(Color.GREEN);
            }

            @Override
            public void onError(String message) {
                updateButtonColor(Color.RED);
            }
        });
    }

    @Click(R.id.reloadButton)
    void onReloadMetadataClick() {
        updateServerAddress();
        final Activity activity = this;
        loadedCheckbox.setChecked(false);
        connectionService.loadMetadataFromServer(new LoadMetadataCallback() {
            @Override
            public void onError(String message) {
                onLoadMetadataError(message);
            }

            @Override
            public void onSuccess(Vocabulary vocabulary, OpenCvConfig openCvConfig) {
                try {

                    settings.setOpenCvConfig(openCvConfig);
                    settings.setMetadataLoaded(true);
                    settings.setVocabulary(vocabulary);
                    updateMetadataLoaded();
                } catch (IOException e) {
                    onSaveLoadedMetadataError(activity);
                }
            }
        });
    }

    @UiThread
    void onSaveLoadedMetadataError(Activity activity) {
        settings.setMetadataLoaded(false);
        new AlertDialog.Builder(activity)
                .setMessage("Error during saving on device")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @UiThread
    void onLoadMetadataError(String message) {
        settings.setMetadataLoaded(false);
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @CheckedChange(R.id.queryingSwitch)
    void onQueryingMethodChange() {
        settings.setQueryingMethod(queryingSwitch.isChecked());
    }

    @Override
    protected void onPause() {
        updateServerAddress();
        super.onPause();
    }

    private void updateServerAddress() {
        String newAddress = serverAddress.getText().toString();
        if (!settings.getServerAddress().equals(newAddress)) {
            connectionService.updateServerAddress(newAddress);
            settings.setServerAddress(newAddress);
        }
    }

    @UiThread
    void updateButtonColor(int color) {
        testButton.setBackgroundColor(color);
    }

    @UiThread
    void updateMetadataLoaded() {
        loadedCheckbox.setChecked(settings.isMetadataLoaded());
    }

}
