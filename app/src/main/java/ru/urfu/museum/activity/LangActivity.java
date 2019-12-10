package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.AppActivity;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.utils.Preference;

public class LangActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang);
        Button langButtonEnglish = findViewById(R.id.langButtonEnglish);
        Button langButtonChinese = findViewById(R.id.langButtonChinese);

        langButtonEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(KeyWords.LANG_EN);
            }
        });
        langButtonChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(KeyWords.LANG_ZH);
            }
        });
    }

    private void startNextActivity(String langCode) {
        Preference.setValue(this, Preference.LANG, langCode);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
