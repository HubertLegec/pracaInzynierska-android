package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistogramGeneratorTest {

    @Test
    public void transformVocabularyToMatTest() {
        Vocabulary vocabulary = getSampleVocabulary();
        Mat mat = HistogramGenerator.transformVocabularyToMat(vocabulary);

        Assert.assertNotNull(mat);
        Assert.assertEquals(vocabulary.getSize(), mat.size().height());
        Assert.assertEquals(vocabulary.getRowSize(), mat.size().width());
    }

    private Vocabulary getSampleVocabulary() {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setSize(100);
        vocabulary.setRowSize(64);
        List<List<Float>> voc = new ArrayList<>(100);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            List<Float> row = new ArrayList<>(64);
            for (int j = 0; j < 64; j++) {
                row.add(rand.nextFloat());
            }
            voc.add(row);
        }
        vocabulary.setVocabulary(voc);
        return vocabulary;
    }
}
