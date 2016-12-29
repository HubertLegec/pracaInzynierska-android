package com.legec.imgsearch.app.restConnection;

import android.util.Log;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.callbacks.LoadMetadataCallback;
import com.legec.imgsearch.app.restConnection.callbacks.NoParamsCallback;
import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;
import com.legec.imgsearch.app.settings.GlobalSettings;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.rest.spring.annotations.RestService;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;


@EBean(scope = EBean.Scope.Singleton)
public class ConnectionService {
    private static final String TAG = "CONNECTION_SERVICE";
    @StringRes(R.string.server_unavailable_message)
    String serverUnavailableMessage;
    @StringRes(R.string.unexpected_error_message)
    String unexpectedErrorMessage;
    @RestService
    RestClient restClient;
    @Bean
    GlobalSettings settings;

    @AfterInject
    void afterDependenciesInject() {
        updateServerAddress(settings.getServerAddress());
    }

    @Background
    public void checkServerStatus(NoParamsCallback callback) {
        try {
            restClient.healthCheck();
            callback.onSuccess();
        } catch (ResourceAccessException e) {
            callback.onError(serverUnavailableMessage);
        } catch (Exception e) {
            callback.onError(unexpectedErrorMessage);
        }
    }

    @Background
    public void loadMetadataFromServer(LoadMetadataCallback callback) {
        try {
            ExtractorDescription extractorDescription = restClient.getExtractorDescription().getBody();
            MatcherDescription matcherDescription = restClient.getMatcherDescription().getBody();
            Vocabulary vocabulary = restClient.getVocabulary().getBody();
            callback.onSuccess(vocabulary, extractorDescription, matcherDescription);
        } catch (ResourceAccessException e) {
            callback.onError(serverUnavailableMessage);
        } catch (NestedRuntimeException e) {
            callback.onError(unexpectedErrorMessage);
        }
    }

    public void updateServerAddress(String newAddress) {
        Log.i(TAG, "update server address: http://" + newAddress);
        restClient.setRootUrl("http://" + newAddress);
        Log.i(TAG, "server address updated: " + restClient.getRootUrl());
    }

    public List<ImageDetails> findByImage(ByteArrayResource image) {
        MultiValueMap<String, Object> mvMap = new LinkedMultiValueMap<>();
        mvMap.add("image", image);
        try {
            restClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
            return restClient.find(mvMap)
                    .getBody()
                    .getImages();
        } catch (Exception e) {
            Log.w(TAG, "findByImage: ", e);
            throw e;
        }
    }

    public List<ImageDetails> findByHistogram(List<Float> histogram) {
        try {
            return restClient.findByHistogram(histogram)
                    .getBody()
                    .getImages();
        } catch (Exception e) {
            Log.e(TAG, "find by histogram", e);
            throw e;
        }
    }
}
