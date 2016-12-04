package com.legec.imgsearch.app.opencv;


import android.util.Log;

import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;
import com.legec.imgsearch.app.settings.GlobalSettings;
import com.legec.imgsearch.app.utils.FileUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.imdecode;

@EBean
public class OpenCvService {
    private static final String TAG = "ImgSearch-OpenCv";
    @Bean
    GlobalSettings globalSettings;
    @Bean
    FileUtils fileUtils;

    public List<Float> generateHistogram(ByteArrayResource image) {
        Log.i(TAG, "generate histogram");
        if (!HistogramGenerator.isValid()) {
            if (globalSettings.isMetadataLoaded()) {
                try {
                    updateConfiguration();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Unexpected error during configuration update");
                }
            } else {
                throw new RuntimeException("Metadata is not loaded properly!!!");
            }
        }
        byte[] imgBytes = image.getByteArray();
        Mat imgMat = new Mat(imgBytes);
        Mat grayscaleImage = imdecode(imgMat, opencv_imgcodecs.IMREAD_GRAYSCALE);
        float[] histogram = HistogramGenerator.getHistogramForImage(grayscaleImage);
        return histogramToList(histogram);
    }

    public void updateConfiguration() throws IOException {
        Log.i(TAG, "update configuration");
        Vocabulary v = fileUtils.getObjectFromFile(FileUtils.VOCABULARY_FILE_NAME, Vocabulary.class);
        String extractor = globalSettings.getExtractorType();
        MatcherDescription matcher = globalSettings.getMatcherType();
        HistogramGenerator.update(v, extractor, matcher);
    }

    private List<Float> histogramToList(float[] histogram) {
        Log.i(TAG, "histogram to list");
        List<Float> result = new ArrayList<>(histogram.length);
        for (float v : histogram) {
            result.add(v);
        }
        return result;
    }

}
