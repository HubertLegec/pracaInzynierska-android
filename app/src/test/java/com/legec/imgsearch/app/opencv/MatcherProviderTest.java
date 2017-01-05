package com.legec.imgsearch.app.opencv;


import android.content.Context;
import android.os.Build;

import com.legec.imgsearch.app.BuildConfig;
import com.legec.imgsearch.app.exception.NotImplementedYetException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;

import junit.framework.Assert;

import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_features2d.FlannBasedMatcher;
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
public class MatcherProviderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private Context ctx;

    @Before
    public void setup() {
        ctx = RuntimeEnvironment.application;
    }

    @Test
    public void getMatcherWithInvalidNameTest() {
        exception.expect(NotImplementedYetException.class);
        OpenCvConfig config = new OpenCvConfig("NotImplementedMatcher", 0, null);
        MatcherProvider_
                .getInstance_(ctx)
                .getMatcherByDescription(config);
    }

    @Test
    public void getBfMatcherTest() {
        String name = "BFMatcher";
        OpenCvConfig config = new OpenCvConfig(name, 4, null);
        DescriptorMatcher matcher = MatcherProvider_
                .getInstance_(ctx)
                .getMatcherByDescription(config);
        Assert.assertTrue(matcher instanceof BFMatcher);
    }

    @Test
    public void getFlannMatcherTest() {
        String name = "FlannBasedMatcher";
        OpenCvConfig config = new OpenCvConfig(name, 4, null);
        DescriptorMatcher matcher = MatcherProvider_
                .getInstance_(ctx)
                .getMatcherByDescription(config);
        Assert.assertTrue(matcher instanceof FlannBasedMatcher);
    }
}
