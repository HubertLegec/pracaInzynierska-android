package com.legec.imgsearch.app.result;

import android.util.Log;

import com.legec.imgsearch.app.exception.ImageLoadingException;
import com.legec.imgsearch.app.exception.MetadataNotLoadedException;
import com.legec.imgsearch.app.opencv.OpenCvService;
import com.legec.imgsearch.app.restConnection.ConnectionService;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;
import com.legec.imgsearch.app.settings.GlobalSettings;
import com.legec.imgsearch.app.utils.ImageSaver;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;


@EBean
public class ResultService {
    private static final String TAG = "ImgSearch-ResultService";
    @Bean
    GlobalSettings settings;
    @Bean
    ConnectionService connectionService;
    @Bean
    ImageSaver imageSaver;
    @Bean
    OpenCvService openCvService;

    public List<ImageDetails> sendSearchRequest() {
        ByteArrayResource image = imageSaver.getImage();
        if(settings.getQueryingMethod()) {
            return searchByHistogram(image);
        } else {
            return connectionService.findByImage(image);
        }
    }

    private List<ImageDetails> searchByHistogram(ByteArrayResource imageResource) throws MetadataNotLoadedException, ImageLoadingException{
        Log.i(TAG, "search by histogram");
        List<Float> histogram = openCvService.generateHistogram(imageResource);
        return connectionService.findByHistogram(histogram);
    }
}
