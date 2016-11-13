package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;
import com.legec.imgsearch.app.settings.GlobalSettings;
import com.legec.imgsearch.app.utils.FileUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.bytedeco.javacpp.opencv_core;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@EBean
public class OpenCvService {

    @Bean
    GlobalSettings globalSettings;
    @Bean
    FileUtils fileUtils;

    public List<Float> generateHistogram(byte[] image) {
        //TODO
        try {
            Vocabulary v = fileUtils.getObjectFromFile(FileUtils.VOCABULARY_FILE_NAME, Vocabulary.class);
            String extractor = globalSettings.getExtractorType();
            MatcherDescription matcher = globalSettings.getMatcherType();
            HistogramGenerator generator = new HistogramGenerator(v, extractor, matcher);
            opencv_core.Mat cvImage = null;
            generator.getHistogramForImage(cvImage);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
