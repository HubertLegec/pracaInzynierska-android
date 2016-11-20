package com.legec.imgsearch.app.opencv;


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
import java.util.Collections;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.imdecode;

@EBean
public class OpenCvService {

    @Bean
    GlobalSettings globalSettings;
    @Bean
    FileUtils fileUtils;

    public List<Float> generateHistogram(ByteArrayResource image) {
        //TODO
        try {
            byte[] imgBytes = image.getByteArray();
            Mat imgMat = new Mat(imgBytes);
            Mat grayscaleImage = imdecode(imgMat, opencv_imgcodecs.IMREAD_GRAYSCALE);
            Vocabulary v = fileUtils.getObjectFromFile(FileUtils.VOCABULARY_FILE_NAME, Vocabulary.class);
            String extractor = globalSettings.getExtractorType();
            MatcherDescription matcher = globalSettings.getMatcherType();
            HistogramGenerator generator = new HistogramGenerator(v, extractor, matcher);
            Mat histogram = generator.getHistogramForImage(grayscaleImage);
            return matHistogramToListHistogram(histogram);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Float> matHistogramToListHistogram(Mat mat) {
        int h = mat.size().height();
        int w = mat.size().width();

        return Collections.emptyList();
    }

}
