package com.legec.imgsearch.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.ConnectionService;
import com.legec.imgsearch.app.restConnection.callbacks.LoadMetadataCallback;
import com.legec.imgsearch.app.restConnection.callbacks.NoParamsCallback;
import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;
import com.legec.imgsearch.app.settings.GlobalSettings;
import com.legec.imgsearch.app.utils.FileUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
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
    @Bean
    ConnectionService connectionService;
    @Bean
    FileUtils fileUtils;
    @Bean
    GlobalSettings settings;


    @AfterViews
    void initViews() {
        serverAddress.setText(settings.getServerAddres());
        loadedCheckbox.setChecked(settings.isMetadataLoaded());
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
            public void onError() {
                updateButtonColor(Color.RED);
            }
        });
    }

    @Click(R.id.reloadButton)
    void onReloadMetadataClick() {
        updateServerAddress();
        final Activity activity = this;
        connectionService.loadMetadataFromServer(new LoadMetadataCallback() {
            @Override
            public void onError() {
                settings.setMetadataLoaded(false);
                new AlertDialog.Builder(activity)
                        .setMessage(R.string.load_metadata_error)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }

            @Override
            public void onSuccess(Vocabulary vocabulary, ExtractorDescription extractorDescription, MatcherDescription matcherDescription) {
                try {
                    fileUtils.saveObjectToFile(vocabulary, FileUtils.VOCABULARY_FILE_NAME);
                    settings.setExtractorType(extractorDescription.getExtractor());
                    settings.setMatcherType(matcherDescription);
                    settings.setMetadataLoaded(true);
                    updateMetadataLoaded();
                } catch (IOException e) {
                    settings.setMetadataLoaded(false);
                    new AlertDialog.Builder(activity)
                            .setMessage("Error during saving on device")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        updateServerAddress();
        super.onPause();
    }

    private void updateServerAddress() {
        String newAddress = serverAddress.getText().toString();
        if (!settings.getServerAddres().equals(newAddress)) {
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
