package com.legec.imgsearch.app.opencv;

import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BOWImgDescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;

import java.util.List;

/**
 * Class responsible for generating images histograms
 * It needs vocabulary and extractor type from server to generate values in accordance with it
 * If the configurations differs, it causes errors in matching images
 */
public class HistogramGenerator {
    private final DescriptorMatcher matcher;
    private final Feature2D extractor;
    private final BOWImgDescriptorExtractor descriptorExtractor;
    private final int vocabularySize;

    /**
     * Creates generator instance
     * @param vocabulary vocabulary fetched from server
     * @param extractorType extractor type fetched from server
     * @param matcherDescription matcher description fetched from server
     */
    public HistogramGenerator(Vocabulary vocabulary, String extractorType, MatcherDescription matcherDescription) {
        matcher = MatcherProvider.getMatcherByDescription(matcherDescription);
        extractor = ExtractorProvider.getExtractorByName(extractorType);
        descriptorExtractor = new BOWImgDescriptorExtractor(extractor, matcher);
        descriptorExtractor.setVocabulary(transformVocabularyToMat(vocabulary));
        vocabularySize = vocabulary.getVocabulary().size();
    }

    private Mat transformVocabularyToMat(Vocabulary vocabulary) {
        List<List<Float>> vocabularyValues = vocabulary.getVocabulary();
        Mat result = new Mat();
        for(List<Float> row : vocabularyValues) {
            float[] array = floatListToArray(row);
            Mat rowMat = new Mat(array);
            result.push_back(rowMat);
        }
        return result;
    }

    private float[] floatListToArray(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public Mat getHistogramForImage(Mat image) {
        opencv_core.KeyPointVector keyPointVector = new opencv_core.KeyPointVector();
        extractor.detect(image, keyPointVector);
        Mat result = new Mat();
        descriptorExtractor.compute(image, result);
        return result;
    }
}
