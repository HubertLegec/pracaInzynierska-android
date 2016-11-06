package com.legec.imgsearch.app.restConnection;


import com.legec.imgsearch.app.restConnection.callbacks.LoadMetadataCallback;
import com.legec.imgsearch.app.restConnection.callbacks.NoParamsCallback;
import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

@EBean
public class ConnectionService {

    @RestService
    RestClient restClient;

    @Background
    public void checkServerStatus(NoParamsCallback callback) {
        try {
            String response = restClient.healthCheck().getBody();
            if ("OK".equals(response)) {
                callback.onSuccess();
            } else {
                callback.onError();
            }
        } catch (Exception e) {
            callback.onError();
        }
    }

    @Background
    public void loadMetadataFromServer(LoadMetadataCallback callback) {
        try {
            ExtractorDescription extractorDescription = restClient.getExtractorDescription().getBody();
            Vocabulary vocabulary = restClient.getVocabulary().getBody();
            callback.onSuccess(vocabulary, extractorDescription);
        } catch (Exception e) {
            callback.onError();
        }
    }

    public void updateServerAddress(String newAddress) {
            restClient.setRootUrl("http://" + newAddress);
    }
}
