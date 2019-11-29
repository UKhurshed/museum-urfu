package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null && getIntent() != null) {
            Intent intent = getIntent();
            try {
                String id = intent.getStringExtra(KeyWords.ID);
                if (id == null) {
                    throw new NullPointerException(this.getClass().getName() + ": Missing required parameter 'id'");
                }
                Bundle bundle = new Bundle();
                bundle.putInt(KeyWords.ID, Integer.parseInt(id));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
