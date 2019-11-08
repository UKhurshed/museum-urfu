package ru.urfu.museum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Preference {

    private static String TAG = Preference.class.getSimpleName();
    private static Map<Integer, String> mKeyCache = new HashMap<Integer, String>();
    private static Preference INSTANCE;
    private static SharedPreferences preferences;
    private final Context mContext;
    public static final String LANG = "LANG";

    public static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Preference(context);
        }
        if (!INSTANCE.mContext.equals(context)) {
            Log.w(TAG, "Singleton instance of Preference already initialized.");
        }
    }

    private Preference(Context context) {
        mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static boolean isInited() {
        return !(INSTANCE == null);
    }

    public static String getValue(String key, String defaultValue) {
        return preferences.getString(mKeyCache.get(key), defaultValue);
    }

    public static void setValue(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
