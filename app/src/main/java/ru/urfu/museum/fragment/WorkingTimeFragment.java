package ru.urfu.museum.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ru.urfu.museum.R;

public class WorkingTimeFragment extends Fragment {

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null && getActivity() != null) {
            rootView = inflater.inflate(R.layout.fragment_working_time, container, false);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            getActivity().setTitle(context.getResources().getString(R.string.working_time));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
