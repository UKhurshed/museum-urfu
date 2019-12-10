package ru.urfu.museum.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.AppActivity;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.fragment.SplashFragment;

public class SplashActivity extends AppActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = fragmentManager.findFragmentByTag(KeyWords.FRAGMENT_SPLASH);
        if (fragment == null) {
            fragment = new SplashFragment();
            fragmentTransaction.add(fragment, KeyWords.FRAGMENT_SPLASH);
        }
        try {
            fragmentTransaction.replace(R.id.splashActivityFrameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (fragment != null) {
            SplashFragment mFragment = (SplashFragment) fragment;
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragment != null) {
            SplashFragment mFragment = (SplashFragment) fragment;
            mFragment.onBackPressed();
        }
    }
}
