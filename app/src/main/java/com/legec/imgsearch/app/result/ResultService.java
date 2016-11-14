package com.legec.imgsearch.app.result;


import com.legec.imgsearch.app.restConnection.ConnectionService;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;
import com.legec.imgsearch.app.settings.GlobalSettings;
import com.legec.imgsearch.app.utils.ImageSaver;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.springframework.core.io.ByteArrayResource;

import java.util.Collections;
import java.util.List;

@EBean
public class ResultService {
    @Bean
    GlobalSettings settings;
    @Bean
    ConnectionService connectionService;
    @Bean
    ImageSaver imageSaver;

    public List<ImageDetails> sendSearchRequest() {
        ByteArrayResource image = imageSaver.getImage();
        if(settings.getQueryingMethod()) {
            return searchByHistogram(image);
        } else {
            return connectionService.findByImage(image);
        }
    }

    private List<ImageDetails> searchByHistogram(ByteArrayResource imageResource) {
        //TODO
        return Collections.emptyList();
    }
}
