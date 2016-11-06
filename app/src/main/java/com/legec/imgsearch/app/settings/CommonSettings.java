package com.legec.imgsearch.app.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.legec.imgsearch.app.R;

/**
 * Class responsible for providing and saving user preferences.
 * It uses Android SHaredPreferences to store data
 */
public class CommonSettings {
    private static final String SERVER_ADDRESS_KEY = "serverAddress";
    private final String defaultServer;
    private SharedPreferences preferences;

    public CommonSettings(Context context) {
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
        editor.commit();
    }
}
