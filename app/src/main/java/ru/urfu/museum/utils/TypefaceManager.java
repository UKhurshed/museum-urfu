package ru.urfu.museum.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class TypefaceManager {

    private static String TAG = TypefaceManager.class.getSimpleName();
    private static Map<String, Typeface> mCache = new HashMap<String, Typeface>();
    public static final String LIGHT = "LIGHT";
    public static final String MEDIUM = "MEDIUM";

    public static Typeface getTypeface(Activity context, String typefaceName) {
        if (!mCache.containsKey(LIGHT)) {
            mCache.put(LIGHT, Typeface.createFromAsset(context.getAssets(), "fonts/RobotoLight.ttf"));
        }
        if (!mCache.containsKey(MEDIUM)) {
            mCache.put(MEDIUM, Typeface.createFromAsset(context.getAssets(), "fonts/RobotoMedium.ttf"));
        }
        if (mCache.containsKey(typefaceName)) {
            return mCache.get(typefaceName);
        } else {
            Log.e(TAG, "Can't find Typeface " + typefaceName);
            return null;
        }
    }

}