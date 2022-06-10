package com.example.android.mockdpsdstudents4;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DPSDPreferencesManager {


    public static final String ALL_DPSDS = "full dpsds list";
    public static final String FILTERED_DPSDS_LIST_KEY = "filtered dpsds list";
    public static final String SHOW_ONLY_SHORTLIST_SETTING_KEY = "show only shortlist";
    public static final String SHOW_ONLY_DISSERTATION_SETTING_KEY = "show only dissertation pending";

    static DPSDPreferencesManager _instance;

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    public static DPSDPreferencesManager instance (Context context) {
        if (_instance == null) {
            _instance = new DPSDPreferencesManager();
            _instance.configSessionUtils(context);
        }
        return _instance;
    }

    public static DPSDPreferencesManager instance() {
        return _instance;
    }

    public void configSessionUtils(Context context) {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefEditor = sharedPref.edit();
    }

    public void storeValueString(String key, String value) {
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    public String fetchValueString(String key) {
        return sharedPref.getString(key, null);
    }

    public boolean fetchBoolean(String key) {
        return sharedPref.getBoolean(key, false);
    }


    public void deleteAllPreferences() {
        sharedPrefEditor.clear();
        sharedPrefEditor.apply();
    }


}
