package ru.urfu.museum.classes;

import android.app.Activity;

public class DimensionsUtil {
    public static float dpFromPx(Activity activity, float px) {
        return px / activity.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(Activity activity, float dp) {
        return dp * activity.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDpResource(Activity activity, int dpResource) {
        return activity.getResources().getDimensionPixelSize(dpResource);
    }
}
