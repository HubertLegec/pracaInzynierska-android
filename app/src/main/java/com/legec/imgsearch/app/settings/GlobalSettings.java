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
    private static final String QUERYING_METHOD_KEY = "queryingMethod";
    private static final String RESPONSE_LIMIT_KEY = "responseLimit";
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
     * Returns server address set by user or default value if not set.
     * @return Application server address
     */
    public String getServerAddress() {
        return preferences.getString(SERVER_ADDRESS_KEY, defaultServer);
    }

    public void setServerAddress(String address) {
        setStringProperty(SERVER_ADDRESS_KEY, address);
    }

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
     * @throws @{@link MetadataNotLoadedException} when configuration is not present
     */
    public OpenCvConfig getOpenCvConfig() throws MetadataNotLoadedException {
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
        editor.putBoolean(QUERYING_METHOD_KEY, method);
        editor.apply();
    }

    /**
     * Returns current querying method
     * @return false if querying by image, true if by histogram
     */
    public boolean getQueryingMethod() {
        return preferences.getBoolean(QUERYING_METHOD_KEY, false);
    }

    /**
     * Return vocabulary. If not loaded, load it from file.
     * @return vocabulary
     * @throws @{@link MetadataNotLoadedException} when loading from file fail
     */
    public Vocabulary getVocabulary() throws MetadataNotLoadedException {
        if (vocabulary != null) {
            return vocabulary;
        }
        try {
            vocabulary = vocabularyFileService.getVocabularyFromFile();
            return vocabulary;
        } catch (IOException e) {
            throw new MetadataNotLoadedException("Vocabulary is not loaded yet");
        }
    }

    /**
     * Save vocabulary to file and store it in class variable
     * @param vocabulary vocabulary to save
     * @throws @{@link IOException} when saving to file fail
     */
    public void setVocabulary(Vocabulary vocabulary) throws IOException {
        vocabularyFileService.saveVocabularyToFile(vocabulary);
        this.vocabulary = vocabulary;
    }

    public int getResponseLimit() {
        return preferences.getInt(RESPONSE_LIMIT_KEY, 4);
    }

    public void setResponseLimit(int limit) {
        setIntProperty(RESPONSE_LIMIT_KEY, limit);
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
