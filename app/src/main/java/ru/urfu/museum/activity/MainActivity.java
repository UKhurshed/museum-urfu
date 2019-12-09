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
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.fragment.AboutMuseumFragment;
import ru.urfu.museum.fragment.FavoritesFragment;
import ru.urfu.museum.fragment.MainFragment;
import ru.urfu.museum.fragment.WorkingTimeFragment;
import ru.urfu.museum.interfaces.SwitchFloorListener;
import ru.urfu.museum.utils.Preference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Spinner toolbarSpinner;
    private TextView toolbarTextView;
    private CoordinatorLayout mainScanButtonParent;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawerLayout);
        this.toolbarSpinner = findViewById(R.id.toolbarTitleSpinner);
        this.toolbarTextView = findViewById(R.id.toolbarTitleTextView);
        this.mainScanButtonParent = findViewById(R.id.mainScanButtonParent);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            this.fragment = new MainFragment();
            displayFragment(this.fragment, null);
        }

        FloatingActionButton mainScanButton = findViewById(R.id.mainScanButton);
        mainScanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scanQRCode();
            }

        });

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
                fragmentClass = WorkingTimeFragment.class;
                break;
            case R.id.navMyFavorites:
                fragmentClass = FavoritesFragment.class;
                break;
            case R.id.navAboutMuseum:
                fragmentClass = AboutMuseumFragment.class;
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
            (new AsyncDisplayFragment(fragmentClass, bundle)).execute();
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
        this.setupFragmentListeners();
        try {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
            this.syncToolbarTitleView();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void setupFragmentListeners() {
        if (fragment instanceof MainFragment) {
            MainFragment mainFragment = (MainFragment) fragment;
            mainFragment.setOnSwitchFloorListener(new SwitchFloorListener() {

                @Override
                public void onDisplayFloor(int floor) {
                    Bundle differentBundle = new Bundle();
                    differentBundle.putInt(KeyWords.FLOOR, floor);
                    (new AsyncDisplayFragment(MainFragment.class, differentBundle)).execute();
                    toolbarSpinner.setSelection(floor - 1);
                }

            });
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
                fragment = (Fragment) fragmentClass.newInstance();
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

    private void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("");
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(new ArrayList<String>() {{
            add(IntentIntegrator.QR_CODE);
        }});
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result.getContents() != null) {
            String qrCodeContent = result.getContents();
            if (!qrCodeContent.matches("^\\d+$")) {
                Snackbar.make(this.mainScanButtonParent, R.string.qr_code_incompatible, Snackbar.LENGTH_SHORT).show();
                return;
            }
            int id = Integer.parseInt(result.getContents());
            if (!MocksProvider.containsEntry(this, id)) {
                Snackbar.make(this.mainScanButtonParent, R.string.entry_not_found_by_qr_code, Snackbar.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(KeyWords.ID, Integer.toString(id));
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
