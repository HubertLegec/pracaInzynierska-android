package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.exception.InvalidExtractorNameException;

import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;
import org.bytedeco.javacpp.opencv_xfeatures2d.SURF;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExtractorProviderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getExtractorWithInvalidNameTest() {
        exception.expect(InvalidExtractorNameException.class);
        String name = "Extractor";
        ExtractorProvider.getExtractorByName(name);
    }

    @Test
    public void getSiftExtractorTest() {
        String name = "cv2.xfeatures2d_SIFT";
        Feature2D extractor = ExtractorProvider.getExtractorByName(name);
        Assert.assertTrue(extractor instanceof SIFT);
    }

    @Test
    public void getSurfExtractorTest() {
        String name = "cv2.xfeatures2d_SURF";
        Feature2D extractor = ExtractorProvider.getExtractorByName(name);
        Assert.assertTrue(extractor instanceof SURF);
    }
}
