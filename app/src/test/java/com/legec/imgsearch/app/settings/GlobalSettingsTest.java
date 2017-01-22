package com.legec.imgsearch.app.settings;

import android.content.Context;
import android.os.Build;

import com.legec.imgsearch.app.BuildConfig;
import com.legec.imgsearch.app.exception.MetadataNotLoadedException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;
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
public class GlobalSettingsTest {
    private Context ctx;

    @Before
    public void setup() {
        ctx = RuntimeEnvironment.application;
    }

    @Test
    public void defaultServerAddressTest(){
        GlobalSettings settings = GlobalSettings_.getInstance_(ctx);
        Assert.assertEquals("51.255.204.169", settings.getServerAddress());
    }

    @Test
    public void changeServerAddressTest() {
        GlobalSettings settings = GlobalSettings_.getInstance_(ctx);
        Assert.assertEquals("51.255.204.169", settings.getServerAddress());

        String newAddress = "1.1.1.1";
        settings.setServerAddress(newAddress);
        Assert.assertEquals(newAddress, settings.getServerAddress());
    }

    @Test
    public void testOpenCvConfig() {
        GlobalSettings settings = GlobalSettings_.getInstance_(ctx);
        boolean wasThrown = false;
        try {
            settings.getOpenCvConfig();
        } catch (MetadataNotLoadedException e) {
            wasThrown = true;
        }
        Assert.assertTrue(wasThrown);

        OpenCvConfig config = new OpenCvConfig("matcher", 1, "extractor");
        settings.setOpenCvConfig(config);

        Assert.assertEquals(config, settings.getOpenCvConfig());
    }

    @Test
    public void vocabularyTest() throws IOException {
        GlobalSettings settings = GlobalSettings_.getInstance_(ctx);
        boolean wasThrown = false;
        try {
            settings.getVocabulary();
        } catch (MetadataNotLoadedException e) {
            wasThrown = true;
        }
        Assert.assertTrue(wasThrown);

        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setSize(200);
        vocabulary.setRowSize(128);
        vocabulary.setVocabulary(new ArrayList<List<Float>>());
        settings.setVocabulary(vocabulary);

        Assert.assertEquals(vocabulary, settings.getVocabulary());
    }

}
