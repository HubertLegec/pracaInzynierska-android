package com.legec.imgsearch.app.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.exception.MetadataNotLoadedException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

import java.io.IOException;


/**
 * Class responsible for providing and saving user preferences.
 * It uses Android SHaredPreferences to store data
 */
@EBean(scope = EBean.Scope.Singleton)
public class GlobalSettings {
    private static final String SERVER_ADDRESS_KEY = "serverAddress";
    private static final String METADATA_LOADED_KEY = "metadataLoaded";
    private static final String EXTRACTOR_TYPE_KEY = "descriptorType";
    private static final String MATCHER_TYPE_KEY = "matcherType";
    private static final String MATCHER_NORM_KEY = "matcherNorm";
    private static final String QUERYING_METHOD_CHANGE = "queryingMethod";
    @StringRes(R.string.default_server_address)
    String defaultServer;
    @StringRes(R.string.preference_file_key)
    String preferenceFileKey;
    @RootContext
    Context context;
    @Bean
    VocabularyFileService vocabularyFileService;

    private SharedPreferences preferences;
    private OpenCvConfig openCvConfig;
    private Vocabulary vocabulary;

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
     * Returns OpenCV config - extractor, matcher and norm type
     * @return @{@link OpenCvConfig} object with configuration parameters
     */
    public OpenCvConfig getOpenCvConfig() {
        OpenCvConfig config = openCvConfig != null ? openCvConfig : loadConfigFromPreferences();
        if (!config.isValid()) {
            throw new MetadataNotLoadedException("OpenCV configuration params are not loaded yet");
        }
        return config;
    }

    /**
     * Stores matcher, norm type and extractor in SharedPreferences.
     * @param config description of OpenCV configuration
     */
    public void setOpenCvConfig(OpenCvConfig config) {
        openCvConfig = config;
        setStringProperty(MATCHER_TYPE_KEY, config.getMatcher_type());
        setIntProperty(MATCHER_NORM_KEY, config.getNorm_type());
        setStringProperty(EXTRACTOR_TYPE_KEY, config.getExtractor());
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

    /**
     * Return vocabulary. If not loaded, load it from file.
     * @return vocabulary
     * @throws IOException when loading from file fail
     */
    public Vocabulary getVocabulary() throws IOException{
        if (vocabulary != null) {
            return vocabulary;
        }
        vocabulary = vocabularyFileService.getVocabularyFromFile();
        return vocabulary;
    }

    /**
     * Save vocabulary to file and store it in class variable
     * @param vocabulary vocabulary to save
     * @throws IOException when saving to file fail
     */
    public void setVocabulary(Vocabulary vocabulary) throws IOException {
        vocabularyFileService.saveVocabularyToFile(vocabulary);
        this.vocabulary = vocabulary;
    }

    private OpenCvConfig loadConfigFromPreferences() {
        String matcher = preferences.getString(MATCHER_TYPE_KEY, null);
        int normType = preferences.getInt(MATCHER_NORM_KEY, 0);
        String extractor = preferences.getString(EXTRACTOR_TYPE_KEY, null);
        return new OpenCvConfig(matcher, normType, extractor);
    }

    private void setStringProperty(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void setIntProperty(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

}
