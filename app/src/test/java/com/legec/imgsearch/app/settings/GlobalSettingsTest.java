package com.legec.imgsearch.app.settings;

import android.content.Context;
import android.os.Build;

import com.legec.imgsearch.app.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class GlobalSettingsTest {

    private Context instrumantationCtx;

    @Before
    public void setup() {
        instrumantationCtx = RuntimeEnvironment.application;
    }

    @Test
    public void testDefaultServerAddress(){
        GlobalSettings settings = GlobalSettings_.getInstance_(instrumantationCtx);
        Assert.assertEquals("51.255.204.169", settings.getServerAddress());
    }

    @Test
    public void testChangeServerAddress() {
        GlobalSettings settings = GlobalSettings_.getInstance_(instrumantationCtx);
        Assert.assertEquals("51.255.204.169", settings.getServerAddress());

        String newAddress = "1.1.1.1";
        settings.setServerAddress(newAddress);
        Assert.assertEquals(newAddress, settings.getServerAddress());
    }

}
