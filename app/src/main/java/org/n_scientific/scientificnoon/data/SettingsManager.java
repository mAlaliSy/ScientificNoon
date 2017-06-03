package org.n_scientific.scientificnoon.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mohammad on 03/06/17.
 */

public class SettingsManager {


    private static final String FONT_SIZE_KEY = "font_size";
    private static final int DEFAULT_FONT_SIZE = 15;

    private static SettingsManager instance;

    Context context;

    SharedPreferences sharedPreferences;

    private SettingsManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingsManager getInstance(Context context) {
        if (instance == null)
            instance = new SettingsManager(context);
        return instance;
    }


    public void setFontSize(int fontSize) {
        sharedPreferences.edit().putInt(FONT_SIZE_KEY, fontSize).apply();
    }

    public int getFontSize() {
        return sharedPreferences.getInt(FONT_SIZE_KEY, DEFAULT_FONT_SIZE);
    }

}
