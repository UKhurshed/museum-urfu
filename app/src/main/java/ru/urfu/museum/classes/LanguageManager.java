package ru.urfu.museum.classes;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import ru.urfu.museum.utils.Preference;

public class LanguageManager {

    public static Context applyLanguage(Context context) {
        String appLang = Preference.getValue(context, Preference.LANG, KeyWords.LANG_EN);
        String deviceLang = Locale.getDefault().getLanguage();
        if (appLang.equals(deviceLang)) {
            return context;
        }
        Locale locale = new Locale(appLang);
        Configuration configuration = context.getResources().getConfiguration();
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

}
