package com.legec.imgsearch.app.opencv;

import android.content.Context;
import android.os.Build;

import com.legec.imgsearch.app.BuildConfig;
import com.legec.imgsearch.app.exception.InvalidExtractorNameException;

import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;
import org.bytedeco.javacpp.opencv_xfeatures2d.SURF;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ExtractorProviderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private Context ctx;

    @Before
    public void setup() {
        ctx = RuntimeEnvironment.application;
    }

    @Test
    public void getExtractorWithInvalidNameTest() {
        exception.expect(InvalidExtractorNameException.class);
        String name = "Extractor";
        ExtractorProvider_.getInstance_(ctx).getExtractorByName(name);
    }

    @Test
    public void getSiftExtractorTest() {
        String name = "cv2.xfeatures2d_SIFT";
        Feature2D extractor = ExtractorProvider_.getInstance_(ctx).getExtractorByName(name);
        Assert.assertTrue(extractor instanceof SIFT);
    }

    @Test
    public void getSurfExtractorTest() {
        String name = "cv2.xfeatures2d_SURF";
        Feature2D extractor = ExtractorProvider_.getInstance_(ctx).getExtractorByName(name);
        Assert.assertTrue(extractor instanceof SURF);
    }
}
