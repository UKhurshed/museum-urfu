package ru.urfu.museum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.urfu.museum.R;
import ru.urfu.museum.adapters.MainAdapter;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;

public class MainFragment extends Fragment {

    private View rootView;
    private MainAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        Bundle bundle = getArguments();
        int floor = bundle != null ? bundle.getInt(KeyWords.FLOOR, 1) : 1;
        adapter = new MainAdapter(getActivity(), MocksProvider.getEntries(getActivity(), floor));
        adapter.setHasStableIds(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            RecyclerView listView = rootView.findViewById(R.id.listView);
            RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 1);
            listView.setAdapter(adapter);
            listView.setLayoutManager(manager);
        }
        return rootView;
    }
}
