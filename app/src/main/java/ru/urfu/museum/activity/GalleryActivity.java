package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.fragment.GalleryFragment;

public class GalleryActivity extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (getIntent() != null) {
            if (this.fragment == null) {
                Intent intent = getIntent();
                String id = intent.getStringExtra(KeyWords.ID);
                String position = intent.getStringExtra(KeyWords.POSITION);
                Bundle bundle = new Bundle();
                bundle.putInt(KeyWords.ID, id == null ? -1 : Integer.parseInt(id));
                bundle.putInt(KeyWords.POSITION, position == null ? -1 : Integer.parseInt(position));
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
