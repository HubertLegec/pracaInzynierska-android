package com.legec.imgsearch.app.restConnection.callbacks;


import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

public interface LoadMetadataCallback {
    void onError();
    void onSuccess(
            Vocabulary vocabulary,
            ExtractorDescription extractorDescription,
            MatcherDescription matcherDescription
    );
}
