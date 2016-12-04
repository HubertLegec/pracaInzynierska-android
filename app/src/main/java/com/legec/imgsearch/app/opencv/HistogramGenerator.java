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
    private static opencv_xfeatures2d.SIFT extractor;
    private static BOWImgDescriptorExtractor descriptorExtractor;

    /**
     * Updates generator instance
     * @param vocabulary vocabulary fetched from server
     * @param extractorType extractor type fetched from server
     * @param matcherDescription matcher description fetched from server
     */
    public static void update(Vocabulary vocabulary, String extractorType, MatcherDescription matcherDescription) {
        DescriptorMatcher matcher = MatcherProvider.getMatcherByDescription(matcherDescription);
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
    public static float[] getHistogramForImage(Mat image) {
        KeyPointVector keyPointVector = new KeyPointVector();
        extractor.detect(image, keyPointVector);
        Mat result = new Mat();
        descriptorExtractor.compute(image, keyPointVector, result);
        FloatBuffer floatBuffer = result.createBuffer();
        float[] histogram = new float[floatBuffer.capacity()];
        floatBuffer.get(histogram);
        return histogram;
    }

    public static boolean isValid() {
        return extractor != null && descriptorExtractor != null;
    }

    private static Mat transformVocabularyToMat(Vocabulary vocabulary) {
        List<List<Float>> values = vocabulary.getVocabulary();
        Mat result = null;
        for (List<Float> row : values){
            if (result == null) {
                result = listOfFloatToMat(row);
            } else {
                Mat rowMat = listOfFloatToMat(row);
                result.push_back(rowMat);
            }
        }
        return result;
    }

    private static Mat listOfFloatToMat(List<Float> row) {
        FloatBuffer buffer = FloatBuffer.allocate(row.size());
        for(Float v : row) {
            buffer.put(v);
        }
        return new Mat(1, row.size(), CV_32F, new FloatPointer(buffer));
    }
}
