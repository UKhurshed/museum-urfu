package ru.urfu.museum.fragment;

import android.Manifest;
import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
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
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

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
        boolean granted = true;
        for (String permission : this.permissions) {
            granted = granted && isPermissionGranted(permission);
        }
        return granted;
    }

    private boolean isPermissionGranted(String permissionName) {
        if (getActivity() == null) {
            return false;
        }
        return ContextCompat.checkSelfPermission(getActivity(), permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMultiplePermissions() {
        if (getActivity() == null) {
            return;
        }
        REQUEST_PERMISSIONS_AGAIN = false;
        ActivityCompat.requestPermissions(getActivity(), this.permissions, KeyWords.PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (requestCode == KeyWords.PERMISSION_REQUEST_CODE) {
            if (this.isPermissionsGranted()) {
                this.startNextActivity();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(getStringFromRes(R.string.attention));
            builder.setMessage(getStringFromRes(R.string.no_permissions));
            builder.setPositiveButton(getStringFromRes(R.string.settings), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    openApplicationSettings();
                }

            });
            builder.setNegativeButton(getStringFromRes(R.string.exit), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    activity.finish();
                }

            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    activity.finish();
                }

            });
            builder.show();
        }
    }

    private void openApplicationSettings() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        REQUEST_PERMISSIONS_AGAIN = true;
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(appSettingsIntent, KeyWords.PERMISSION_REQUEST_CODE);
    }

    private void startNextActivity() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        Intent intent;
        if (Preference.getValue(activity, Preference.LANG, null) == null) {
            intent = new Intent(activity, LangActivity.class);
        } else {
            intent = new Intent(activity, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    private String getStringFromRes(int resId) {
        return getActivity() == null ? "" : getActivity().getResources().getString(resId);
    }

    public void onBackPressed() {
        if (delayedAskPermissions != null) {
            delayedAskPermissions.stop();
            if (getActivity() != null) {
                getActivity().finish();
            }
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
