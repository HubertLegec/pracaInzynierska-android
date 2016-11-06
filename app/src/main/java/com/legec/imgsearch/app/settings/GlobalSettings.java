package com.legec.imgsearch.app.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.legec.imgsearch.app.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


/**
 * Class responsible for providing and saving user preferences.
 * It uses Android SHaredPreferences to store data
 */
@EBean
public class GlobalSettings {
    private static final String SERVER_ADDRESS_KEY = "serverAddress";
    private static final String METADATA_LOADED_KEY = "metadataLoaded";
    private static final String EXTRACTOR_TYPE_KEY = "descriptorType";
    private String defaultServer;
    private SharedPreferences preferences;

    @RootContext
    Context context;


    @AfterInject
    void afterSettingsContextInjection() {
        this.preferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        this.defaultServer = context.getString(R.string.default_server_address);
    }

    /**
     * Returns server address set by user.
     * If not present default value from resources is returned.
     * @return Application server address
     */
    public String getServerAddres() {
        return preferences.getString(SERVER_ADDRESS_KEY, defaultServer);
    }

    /**
     * Stores server address in SharedPreferences. If given string is null or is empty nothing happens.
     * @param address Server address. Should not be null or empty.
     */
    public void setServerAddress(String address) {
        if(address == null || address.isEmpty()) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putString(SERVER_ADDRESS_KEY, address);
        editor.apply();
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
     * Stores descriptor type in SharedPreferences. If given value is null or is empty nothing happens.
     * @param extractorType Descriptor type as string. Should not be null or empty
     */
    public void setExtractorType(String extractorType) {
        if (extractorType == null || extractorType.isEmpty()) {
            return;
        }
        Editor editor = preferences.edit();
        editor.putString(EXTRACTOR_TYPE_KEY, extractorType);
        editor.apply();
    }

}
