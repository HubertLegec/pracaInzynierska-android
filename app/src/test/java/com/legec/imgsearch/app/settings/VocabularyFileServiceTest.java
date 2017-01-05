package com.legec.imgsearch.app.settings;


import android.content.Context;
import android.os.Build;

import com.legec.imgsearch.app.BuildConfig;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class VocabularyFileServiceTest {
    private Context ctx;

    @Before
    public void setup() {
        ctx = RuntimeEnvironment.application;
    }

    @Test
    public void testSaveAndLoadProcess() throws IOException{
        Vocabulary vocabulary = generateSampleVocabulary(50, 64);
        VocabularyFileService_ service = VocabularyFileService_.getInstance_(ctx);
        service.saveVocabularyToFile(vocabulary);
        Vocabulary loadedVocabulary = service.getVocabularyFromFile();

        Assert.assertEquals(vocabulary.getRowSize(), loadedVocabulary.getRowSize());
        Assert.assertEquals(vocabulary.getSize(), loadedVocabulary.getSize());
    }

    private Vocabulary generateSampleVocabulary(int size, int rowSize) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setRowSize(rowSize);
        vocabulary.setSize(size);
        List<List<Float>> voc = new ArrayList<>(size);
        for(int i = 1; i <= size; i++) {
            List<Float> row = new ArrayList<>(rowSize);
            for(int j = 1; j <= rowSize; j++) {
                row.add((float) j / i);
            }
            voc.add(row);
        }
        vocabulary.setVocabulary(voc);
        return vocabulary;
    }
}
