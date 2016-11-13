package com.legec.imgsearch.app.restConnection;


import android.util.Log;

import com.legec.imgsearch.app.restConnection.callbacks.LoadMetadataCallback;
import com.legec.imgsearch.app.restConnection.callbacks.NoParamsCallback;
import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

@EBean
public class ConnectionService {
    private static final String TAG = "CONNECTION_SERVICE";

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
            MatcherDescription matcherDescription = restClient.getMatcherDescription().getBody();
            Vocabulary vocabulary = restClient.getVocabulary().getBody();
            callback.onSuccess(vocabulary, extractorDescription, matcherDescription);
        } catch (Exception e) {
            callback.onError();
        }
    }

    public void updateServerAddress(String newAddress) {
            restClient.setRootUrl("http://" + newAddress);
    }

    public List<String> findByImage(ByteArrayResource image) {
        MultiValueMap<String, Object> mvMap = new LinkedMultiValueMap<>();
        mvMap.add("image", image);
        try {
            restClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
            return restClient.find(mvMap).getBody().getImages();
        } catch (Exception e) {
            Log.w(TAG, "findByImage: ", e);
            return Collections.emptyList();
        }
    }

    public List<String> findByHistogram(List<Float> histogram) {
        try {
            return restClient.findByHistogram(histogram).getBody().getImages();
        } catch (Exception e) {
            Log.w(TAG, "findByHistogram", e);
            return Collections.emptyList();
        }
    }
}
