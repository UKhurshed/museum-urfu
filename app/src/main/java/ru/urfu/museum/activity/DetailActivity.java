package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.AppActivity;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.fragment.DetailFragment;
import ru.urfu.museum.interfaces.DetailPageEntryListener;
import ru.urfu.museum.interfaces.DetailPageScrollListener;
import ru.urfu.museum.view.FadingScrimInsetsFrameLayout;

public class DetailActivity extends AppActivity {

    private Toolbar toolbar;
    private DetailFragment fragment;
    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isTablet = getResources().getBoolean(R.bool.isTablet);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent() != null) {
            Intent intent = getIntent();
            String id = intent.getStringExtra(KeyWords.ID);
            if (id == null) {
                throw new NullPointerException(this.getClass().getName() + ": Missing required parameter 'id'");
            }
            Entry entry = MocksProvider.getEntry(this, Integer.parseInt(id));
            if (entry != null) {
                toolbar.setTitle(entry.title);
            }
            if (this.fragment == null) {
                Bundle bundle = new Bundle();
                bundle.putInt(KeyWords.ID, Integer.parseInt(id));
                this.fragment = new DetailFragment();
                this.fragment.setArguments(bundle);
            }
            this.beginFragmentTransaction();
        }
    }

    private void beginFragmentTransaction() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            if (!this.isTablet) {
                final FadingScrimInsetsFrameLayout scrimInsetsLayout = findViewById(R.id.scrimInsetsLayout);
                final View toolbarBackground = findViewById(R.id.toolbarBackground);
                this.fragment.setScrollListener(new DetailPageScrollListener() {

                    @Override
                    public void onScrollProgress(float percentage) {
                        int alpha = (int) Math.min(255.0f, percentage * 2.55f);
                        scrimInsetsLayout.redrawWithAlpha(alpha);
                        toolbarBackground.setAlpha(percentage / 100);
                    }

                    @Override
                    public void onSetTitle(String title) {
                        toolbar.setTitle(title);
                    }

                });
                scrimInsetsLayout.redrawWithAlpha(0);
                toolbarBackground.setAlpha(0);
            }
            this.fragment.setOnDisplayEntryListener(new DetailPageEntryListener() {

                @Override
                public void onDisplayEntry(int id) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(KeyWords.ID, id);
                    fragment = new DetailFragment();
                    fragment.setArguments(bundle);
                    beginFragmentTransaction();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
