package ru.urfu.museum.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static SharedPreferences preferences;
    public static final String LANG = "LANG";

    public static String getValue(Context context, String key, String defaultValue) {
        preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }

    public static void setValue(Context context, String key, String value) {
        preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
