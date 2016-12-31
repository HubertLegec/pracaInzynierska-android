package com.legec.imgsearch.app.opencv;

import android.util.Log;

import com.legec.imgsearch.app.exception.ImageLoadingException;
import com.legec.imgsearch.app.exception.MetadataNotLoadedException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;
import com.legec.imgsearch.app.settings.GlobalSettings;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.imdecode;


@EBean
public class OpenCvService {
    private static final String TAG = "ImgSearch-OpenCv";
    @Bean
    GlobalSettings globalSettings;
    @Bean
    ExtractorProvider extractorProvider;
    @Bean
    MatcherProvider matcherProvider;

    public List<Float> generateHistogram(ByteArrayResource image)
            throws MetadataNotLoadedException, ImageLoadingException {
        Log.i(TAG, "generate histogram");
        byte[] imgBytes = getAndValidateImageBytes(image);
        Mat imgMat = new Mat(imgBytes);
        Mat grayscaleImage = imdecode(imgMat, opencv_imgcodecs.IMREAD_GRAYSCALE);
        try {
            HistogramGenerator generator = getHistogramGenerator();
            return generator.getHistogramForImage(grayscaleImage);
        } catch (IOException e) {
            throw new MetadataNotLoadedException("Can't open metadata files");
        }
    }

    private byte[] getAndValidateImageBytes(ByteArrayResource image) throws ImageLoadingException {
        byte[] imgBytes = image.getByteArray();
        if(imgBytes == null || imgBytes.length == 0) {
            throw new ImageLoadingException("Image is not loaded properly");
        }
        return imgBytes;
    }

    private HistogramGenerator getHistogramGenerator() throws IOException {
        Log.d(TAG, "get histogram generator");
        if (!globalSettings.isMetadataLoaded()) {
            throw new MetadataNotLoadedException("Metadata isn't loaded");
        }
        OpenCvConfig config = globalSettings.getOpenCvConfig();
        DescriptorMatcher matcher = matcherProvider.getMatcherByDescription(config);
        Feature2D extractor = extractorProvider.getExtractorByName(config.getExtractor());
        return new HistogramGenerator(globalSettings.getVocabulary(), extractor, matcher);
    }
}
