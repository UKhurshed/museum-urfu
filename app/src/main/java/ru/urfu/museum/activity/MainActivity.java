package ru.urfu.museum.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;

import ru.urfu.museum.R;
import ru.urfu.museum.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            displayFragment(new MainFragment(), null);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        boolean shouldSwitchFragment = this.shouldSwitchFragment(id);
        Class fragmentClass = null;
        Bundle bundle = shouldSwitchFragment ? new Bundle() : null;
        switch (id) {
            case R.id.navMuseum:
                fragmentClass = MainFragment.class;
                break;
            case R.id.navWorkingTime:
                break;
            case R.id.navMyFavorites:
                break;
            case R.id.navAboutMuseum:
                break;
            case R.id.navChangeLanguage:
                break;
            case R.id.navContacts:
                break;
            case R.id.navAboutApp:
                break;
        }
        if (this.shouldSwitchFragment(id)) {
            new AsyncDisplayFragment(fragmentClass, bundle);
            this.drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean shouldSwitchFragment(int id) {
        int[] ids = {
                R.id.navMuseum,
                R.id.navWorkingTime,
                R.id.navMyFavorites,
                R.id.navAboutMuseum
        };
        return Arrays.asList(ids).contains(id);
    }

    private void displayFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        try {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private class AsyncDisplayFragment extends AsyncTask<Void, Void, Void> {

        private Class fragmentClass;
        private Bundle bundle;

        AsyncDisplayFragment(Class fragmentClass, Bundle bundle) {
            this.fragmentClass = fragmentClass;
            this.bundle = bundle;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Fragment fragment = (Fragment) fragmentClass.getConstructor().newInstance();
                displayFragment(fragment, this.bundle);
                super.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
