package com.legec.imgsearch.app.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;


/**
 * Class responsible for providing and saving user preferences.
 * It uses Android SHaredPreferences to store data
 */
@EBean
public class GlobalSettings {
    private static final String SERVER_ADDRESS_KEY = "serverAddress";
    private static final String METADATA_LOADED_KEY = "metadataLoaded";
    private static final String EXTRACTOR_TYPE_KEY = "descriptorType";
    private static final String MATCHER_TYPE_KEY = "matcherType";
    private static final String MATCHER_NORM_KEY = "matcherNorm";
    private static final String QUERYING_METHOD_CHANGE = "queryingMethod";
    @StringRes(R.string.default_server_address)
    private String defaultServer;
    private SharedPreferences preferences;
    @StringRes(R.string.preference_file_key)
    private String preferenceFileKey;
    @RootContext
    Context context;


    @AfterInject
    void afterSettingsContextInjection() {
        this.preferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
    }

    /**
     * Returns server address set by user.
     * If not present default value from resources is returned.
     * @return Application server address
     */
    public String getServerAddress() {
        return preferences.getString(SERVER_ADDRESS_KEY, defaultServer);
    }

    /**
     * Stores server address in SharedPreferences. If given string is null or is empty nothing happens.
     * @param address Server address. Should not be null or empty.
     */
    public void setServerAddress(String address) {
        setStringProperty(SERVER_ADDRESS_KEY, address);
    }

    /**
     * Checks if metadata from server has been loaded.
     * @return true if metadata has been loaded, false otherwise
     */
    public boolean isMetadataLoaded() {
        return preferences.getBoolean(METADATA_LOADED_KEY, false);
    }

    /**
     * Should be called after metadata load
     * @param isLoaded is metadata loaded
     */
    public void setMetadataLoaded(boolean isLoaded) {
        Editor editor = preferences.edit();
        editor.putBoolean(METADATA_LOADED_KEY, isLoaded);
        editor.apply();
    }

    /**
     * Returns descriptor type as string
     * @return descriptor type, or null if not present
     */
    public String getExtractorType() {
        return preferences.getString(EXTRACTOR_TYPE_KEY, null);
    }

    /**
     * Stores extractor type in SharedPreferences. If given value is null or is empty nothing happens.
     * @param extractorType Descriptor type as string. Should not be null or empty
     */
    public void setExtractorType(String extractorType) {
        setStringProperty(EXTRACTOR_TYPE_KEY, extractorType);
    }

    /**
     * Returns matcher type as string
     * @return matcher type, or null if not present
     */
    public MatcherDescription getMatcherType() {
        String name = preferences.getString(MATCHER_TYPE_KEY, null);
        int normType = preferences.getInt(MATCHER_NORM_KEY, 0);
        if (name == null) {
            return null;
        }
        MatcherDescription description = new MatcherDescription();
        description.getMatcher().setMatcher_type(name);
        description.getMatcher().setNorm_type(normType);
        return description;
    }

    /**
     * Stores matcher type in SharedPreferences. If matcher name is null or is empty nothing happens.
     * @param matcherType Matcher definition
     */
    public void setMatcherType(MatcherDescription matcherType) {
        if (setStringProperty(MATCHER_TYPE_KEY, matcherType.getMatcher().getMatcher_type())) {
            setIntProperty(MATCHER_NORM_KEY, matcherType.getMatcher().getNorm_type());
        }
    }

    /**
     * Changes querying method. If current value is by image, by histogram is set.
     * Otherwise conversely.
     */
    public void setQueryingMethod(boolean method) {
        Editor editor = preferences.edit();
        editor.putBoolean(QUERYING_METHOD_CHANGE, method);
        editor.apply();
    }

    /**
     * Returns current querying method
     * @return false if querying by image, true if by histogram
     */
    public boolean getQueryingMethod() {
        return preferences.getBoolean(QUERYING_METHOD_CHANGE, false);
    }

    private boolean setStringProperty(String key, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    private void setIntProperty(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

}
