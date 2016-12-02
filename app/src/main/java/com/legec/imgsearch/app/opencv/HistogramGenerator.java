package com.legec.imgsearch.app.opencv;

import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BOWImgDescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_xfeatures2d;

import java.nio.FloatBuffer;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.CV_32F;

/**
 * Class responsible for generating images histograms
 * It needs vocabulary and extractor type from server to generate values in accordance with it
 * If the configurations differs, it causes errors in matching images
 */
public class HistogramGenerator {
    private final DescriptorMatcher matcher;
    private final opencv_xfeatures2d.SIFT extractor;
    private final BOWImgDescriptorExtractor descriptorExtractor;

    /**
     * Creates generator instance
     * @param vocabulary vocabulary fetched from server
     * @param extractorType extractor type fetched from server
     * @param matcherDescription matcher description fetched from server
     */
    public HistogramGenerator(Vocabulary vocabulary, String extractorType, MatcherDescription matcherDescription) {
        matcher = MatcherProvider.getMatcherByDescription(matcherDescription);
        extractor = opencv_xfeatures2d.SIFT.create(); //(opencv_xfeatures2d.SIFT) ExtractorProvider.getExtractorByName(extractorType);
        descriptorExtractor = new BOWImgDescriptorExtractor(extractor, matcher);
        Mat vocabularyMat = transformVocabularyToMat(vocabulary);
        descriptorExtractor.setVocabulary(vocabularyMat);
    }

    /**
     * Creates histogram for given image.
     * @param image Image as {@link Mat} object. It should be grayscale image
     * @return image histogram
     */
    public float[] getHistogramForImage(Mat image) {
        KeyPointVector keyPointVector = new KeyPointVector();
        extractor.detect(image, keyPointVector);
        Mat result = new Mat();
        descriptorExtractor.compute(image, keyPointVector, result);
        FloatBuffer floatBuffer = result.createBuffer();
        float[] histogram = new float[floatBuffer.capacity()];
        floatBuffer.get(histogram);
        return histogram;
    }

    private Mat transformVocabularyToMat(Vocabulary vocabulary) {
        List<List<Float>> vocabularyValues = vocabulary.getVocabulary();
        FloatBuffer content = floatList2FloatBuffer(vocabularyValues, vocabulary.getSize() * vocabulary.getRowSize());
        Mat result = new Mat(vocabulary.getSize(), vocabulary.getRowSize(), CV_32F, new FloatPointer(content));
        return result;
    }

    private static FloatBuffer floatList2FloatBuffer(List<List<Float>> values, int size){
        FloatBuffer buffer = FloatBuffer.allocate(size);
        for (List<Float> row : values){
            for(Float v : row){
                buffer.put(v);
            }
        }
        return buffer;
    }
}
