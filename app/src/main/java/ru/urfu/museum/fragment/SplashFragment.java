package ru.urfu.museum.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ru.urfu.museum.R;
import ru.urfu.museum.activity.LangActivity;
import ru.urfu.museum.activity.MainActivity;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.utils.DelayedTask;
import ru.urfu.museum.utils.Preference;

public class SplashFragment extends Fragment {
    private View rootView;
    private DelayedTask delayedAskPermissions;
    private boolean ASKED_PERMISSIONS_ON_INIT = false;
    private boolean REQUEST_PERMISSIONS_AGAIN = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        }
        return rootView;
    }

    private boolean isPermissionsGranted() {
        return isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private boolean isPermissionGranted(String permissionName) {
        return ContextCompat.checkSelfPermission(getActivity(), permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermissions() {
        REQUEST_PERMISSIONS_AGAIN = false;
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, KeyWords.PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == KeyWords.PERMISSION_REQUEST_CODE && grantResults.length >= 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startNextActivity();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getStringFromRes(R.string.no_permissions));
                builder.setTitle(getStringFromRes(R.string.warning));
                builder.setPositiveButton(getStringFromRes(R.string.settings), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        openApplicationSettings();
                    }

                });
                builder.setNegativeButton(getStringFromRes(R.string.exit), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }

                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        getActivity().finish();
                    }

                });
                builder.show();
            }
        } else {
            getActivity().finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void openApplicationSettings() {
        REQUEST_PERMISSIONS_AGAIN = true;
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        getActivity().startActivityForResult(appSettingsIntent, KeyWords.PERMISSION_REQUEST_CODE);
    }

    private void startNextActivity() {
        Intent intent;
        if (Preference.getValue(Preference.LANG, null) == null) {
            intent = new Intent(getActivity(), LangActivity.class);
        } else {
            intent = new Intent(getActivity(), MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }

    private String getStringFromRes(int resId) {
        return getActivity().getResources().getString(resId);
    }

    public void onBackPressed() {
        if (delayedAskPermissions != null) {
            delayedAskPermissions.stop();
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        if (!ASKED_PERMISSIONS_ON_INIT) {
            ASKED_PERMISSIONS_ON_INIT = true;
            delayedAskPermissions = new DelayedTask(2000) {

                @Override
                public void execute() {
                    delayedAskPermissions = null;
                    if (isPermissionsGranted()) {
                        startNextActivity();
                    } else {
                        requestMultiplePermissions();
                    }
                }

            };
            delayedAskPermissions.start();
        } else if (isPermissionsGranted()) {
            startNextActivity();
        } else if (REQUEST_PERMISSIONS_AGAIN) {
            requestMultiplePermissions();
        }
        super.onResume();
    }
}