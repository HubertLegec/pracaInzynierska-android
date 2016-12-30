package com.legec.imgsearch.app.opencv;

import android.util.Log;

import com.legec.imgsearch.app.exception.HistogramGeneratorNotConfiguredException;
import com.legec.imgsearch.app.exception.ImageLoadingException;
import com.legec.imgsearch.app.exception.MetadataNotLoadedException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;
import com.legec.imgsearch.app.settings.GlobalSettings;
import com.legec.imgsearch.app.utils.FileUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.apache.commons.lang3.ArrayUtils;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.imdecode;


@EBean
public class OpenCvService {
    private static final String TAG = "ImgSearch-OpenCv";
    @Bean
    GlobalSettings globalSettings;
    @Bean
    FileUtils fileUtils;

    public List<Float> generateHistogram(ByteArrayResource image) throws MetadataNotLoadedException, ImageLoadingException {
        Log.i(TAG, "generate histogram");
        byte[] imgBytes = image.getByteArray();
        if(imgBytes == null || imgBytes.length == 0) {
            throw new ImageLoadingException("Image is not loaded properly");
        }
        Mat imgMat = new Mat(imgBytes);
        Mat grayscaleImage = imdecode(imgMat, opencv_imgcodecs.IMREAD_GRAYSCALE);
        try {
            HistogramGenerator generator = getHistogramGenerator();
            float[] histogram = generator.getHistogramForImage(grayscaleImage);
            return histogramToList(histogram);
        } catch (IOException e) {
            throw new MetadataNotLoadedException("Can't open metadata files");
        }
    }

    private HistogramGenerator getHistogramGenerator() throws IOException {
        Log.d(TAG, "get histogram generator");
        if (!globalSettings.isMetadataLoaded()) {
            throw new MetadataNotLoadedException("Metadata isn't loaded");
        }
        try {
            return HistogramGenerator.getInstance();
        } catch (HistogramGeneratorNotConfiguredException e) {
            return updateAndGetHistogramGenerator();
        }
    }

    public HistogramGenerator updateAndGetHistogramGenerator() throws IOException {
        Log.i(TAG, "update configuration");
        Vocabulary v = fileUtils.getObjectFromFile(FileUtils.VOCABULARY_FILE_NAME, Vocabulary.class);
        OpenCvConfig openCvConfig = globalSettings.getOpenCvConfig();
        return HistogramGenerator.createInstance(v, openCvConfig);
    }

    private List<Float> histogramToList(float[] histogram) {
        Log.i(TAG, "histogram to list");
        return Arrays.asList(ArrayUtils.toObject(histogram));
    }

}
