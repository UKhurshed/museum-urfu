package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.AppActivity;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.fragment.GalleryFragment;

public class GalleryActivity extends AppActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (getIntent() != null) {
            if (this.fragment == null) {
                Intent intent = getIntent();
                String position = intent.getStringExtra(KeyWords.POSITION);
                ArrayList<Integer> list = intent.getIntegerArrayListExtra(KeyWords.IMAGES);
                Bundle bundle = new Bundle();
                bundle.putInt(KeyWords.POSITION, position == null ? 0 : Integer.parseInt(position));
                bundle.putIntegerArrayList(KeyWords.IMAGES, list);
                this.fragment = new GalleryFragment();
                this.fragment.setArguments(bundle);
            }
            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.galleryFrameLayout, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
