package com.legec.imgsearch.app.opencv;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.BOWImgDescriptorExtractor;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

public class HistogramGenerator {
    BFMatcher matcher = new BFMatcher();
    SIFT sift = new SIFT();
    BOWImgDescriptorExtractor descriptorExtractor = new BOWImgDescriptorExtractor(sift, matcher);

    public HistogramGenerator() {
    }

    public HistogramGenerator(Mat vocabulary) {
        descriptorExtractor.setVocabulary(vocabulary);
    }

    public Mat getHistogramForImage(Mat image) {
        return null;
    }
}
