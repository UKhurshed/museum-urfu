package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.fragment.DetailFragment;
import ru.urfu.museum.interfaces.DetailPageScrollListener;
import ru.urfu.museum.view.FadingScrimInsetsFrameLayout;

public class DetailActivity extends AppCompatActivity {

    private DetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent() != null) {
            if (this.fragment == null) {
                Intent intent = getIntent();
                String id = intent.getStringExtra(KeyWords.ID);
                if (id == null) {
                    throw new NullPointerException(this.getClass().getName() + ": Missing required parameter 'id'");
                }
                Bundle bundle = new Bundle();
                bundle.putInt(KeyWords.ID, Integer.parseInt(id));
                this.fragment = new DetailFragment();
                this.fragment.setArguments(bundle);
            }
            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commitAllowingStateLoss();

                final FadingScrimInsetsFrameLayout scrimInsetsLayout = findViewById(R.id.scrimInsetsLayout);
                final View toolbarBackground = findViewById(R.id.toolbarBackground);
                this.fragment.setScrollListener(new DetailPageScrollListener() {

                    public void onScrollProgress(float percentage) {
                        int alpha = (int) Math.min(255.0f, percentage * 2.55f);
                        scrimInsetsLayout.redrawWithAlpha(alpha);
                        toolbarBackground.setAlpha(percentage / 100);
                    }

                    public void onSetTitle(String title) {
                        toolbar.setTitle(title);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
