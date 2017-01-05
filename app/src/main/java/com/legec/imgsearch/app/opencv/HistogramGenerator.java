package com.legec.imgsearch.app.opencv;

import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.apache.commons.lang3.ArrayUtils;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BOWImgDescriptorExtractor;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.CV_32F;


/**
 * Class responsible for generating images histograms
 * It needs vocabulary and extractor type from server to generate values in accordance with it
 * If the configurations differs, it causes errors in matching images
 */
class HistogramGenerator {
    private Feature2D extractor;
    private BOWImgDescriptorExtractor descriptorExtractor;
    private int vocabularySize;


    HistogramGenerator(Vocabulary vocabulary, Feature2D extractor, DescriptorMatcher matcher) {
        this.extractor = extractor;
        descriptorExtractor = new BOWImgDescriptorExtractor(extractor, matcher);
        Mat vocabularyMat = transformVocabularyToMat(vocabulary);
        vocabularySize = vocabulary.getSize();
        descriptorExtractor.setVocabulary(vocabularyMat);
    }

    List<Float> getHistogramForImage(Mat image) {
        KeyPointVector keyPointVector = new KeyPointVector();
        extractor.detect(image, keyPointVector);
        Mat result = new Mat(1, vocabularySize, CV_32F);
        descriptorExtractor.compute(image, keyPointVector, result);
        FloatBuffer floatBuffer = result.createBuffer();
        float[] histogram = new float[floatBuffer.capacity()];
        floatBuffer.get(histogram);
        return histogramToList(histogram);
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
        float[] array = ArrayUtils.toPrimitive(row.toArray(new Float[0]));
        return new Mat(1, row.size(), CV_32F, new FloatPointer(array));
    }

    private List<Float> histogramToList(float[] histogram) {
        return Arrays.asList(ArrayUtils.toObject(histogram));
    }
}
