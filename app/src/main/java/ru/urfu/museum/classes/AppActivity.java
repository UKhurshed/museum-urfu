package ru.urfu.museum.classes;

import android.content.Context;

public abstract class AppActivity extends androidx.appcompat.app.AppCompatActivity {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageManager.applyLanguage(context));
    }

}
