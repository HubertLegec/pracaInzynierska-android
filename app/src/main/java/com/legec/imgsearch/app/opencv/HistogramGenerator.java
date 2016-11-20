package com.legec.imgsearch.app.opencv;

import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BOWImgDescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_xfeatures2d;

import java.nio.ByteBuffer;
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
    public Mat getHistogramForImage(Mat image) {
        KeyPointVector keyPointVector = new KeyPointVector();
        extractor.detect(image, keyPointVector, null);
        Mat result = new Mat();
        descriptorExtractor.compute(image, keyPointVector, result);
        return result;
    }

    private Mat transformVocabularyToMat(Vocabulary vocabulary) {
        List<List<Float>> vocabularyValues = vocabulary.getVocabulary();
        Mat result = new Mat(vocabulary.getSize(), vocabulary.getRowSize(), CV_32F);
        byte[] content = floatList2ByteArray(vocabularyValues, vocabulary.getSize() * vocabulary.getRowSize());
        result.data().put(content);
        return result;
    }

    public static byte[] floatList2ByteArray(List<List<Float>> values, int size){
        ByteBuffer buffer = ByteBuffer.allocate(4 * size);
        for (List<Float> row : values){
            for(Float v : row){
                buffer.putFloat(v);
            }
        }
        return buffer.array();
    }
}
