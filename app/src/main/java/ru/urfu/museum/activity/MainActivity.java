package ru.urfu.museum.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

import ru.urfu.museum.R;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.fragment.MainFragment;
import ru.urfu.museum.interfaces.ITitledFragment;
import ru.urfu.museum.utils.Preference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Spinner toolbarSpinner;
    private TextView toolbarTextView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawerLayout);
        this.toolbarSpinner = findViewById(R.id.toolbarTitleSpinner);
        this.toolbarTextView = findViewById(R.id.toolbarTitleTextView);this.setupToolbarSpinner();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            this.fragment = new MainFragment();
            displayFragment(this.fragment, null);
        }
        this.setupToolbarSpinner();
        this.syncToolbarTitleView();
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
                Preference.setValue(this, Preference.LANG, null);
                Intent langIntent = new Intent(this, LangActivity.class);
                langIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(langIntent);
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

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.toolbarTextView.setText(title);
    }

    private void setupToolbarSpinner() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolbar_spinner_items, R.layout.spinner_toolbar_title_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.toolbarSpinner.setAdapter(adapter);
        this.toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt(KeyWords.FLOOR, position + 1);
                fragment = new MainFragment();
                displayFragment(fragment, bundle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private boolean shouldSwitchFragment(int id) {
        switch (id) {
            case R.id.navMuseum:
            case R.id.navWorkingTime:
            case R.id.navMyFavorites:
            case R.id.navAboutMuseum:
                return true;
        }
        return false;
    }

    private void displayFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        try {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
            setTitle(((ITitledFragment) this.fragment).getTitle());
            this.syncToolbarTitleView();
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
                fragment = (Fragment) fragmentClass.getConstructor().newInstance();
                displayFragment(fragment, this.bundle);
                super.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void syncToolbarTitleView() {
        if (this.fragment == null) {
            return;
        }
        if (this.fragment instanceof MainFragment) {
            if (this.toolbarSpinner.getVisibility() == View.GONE) {
                this.toolbarSpinner.setVisibility(View.VISIBLE);
            }
            if (this.toolbarTextView.getVisibility() == View.VISIBLE) {
                this.toolbarTextView.setVisibility(View.GONE);
            }
        } else {
            if (this.toolbarSpinner.getVisibility() == View.VISIBLE) {
                this.toolbarSpinner.setVisibility(View.GONE);
            }
            if (this.toolbarTextView.getVisibility() == View.GONE) {
                this.toolbarTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
