package com.legec.imgsearch.app.restConnection.callbacks;

import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;


public interface LoadMetadataCallback {
    void onError(String message);
    void onSuccess(
            Vocabulary vocabulary,
            OpenCvConfig openCvConfig
    );
}
