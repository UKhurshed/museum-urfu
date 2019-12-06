package ru.urfu.museum.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.urfu.museum.R;
import ru.urfu.museum.adapters.MainAdapter;
import ru.urfu.museum.classes.DimensionsUtil;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.classes.SpacingItemDecoration;

public class MainFragment extends Fragment {

    private int floor = -1;
    private View rootView;
    private MainAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        Bundle bundle = getArguments();
        this.floor = bundle != null ? bundle.getInt(KeyWords.FLOOR, 1) : 1;
        adapter = new MainAdapter(getActivity(), MocksProvider.getEntries(getActivity(), floor));
        adapter.setHasStableIds(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null && getActivity() != null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int spanCount = 1;
            float spacings = DimensionsUtil.pxFromDpResource(getActivity(), R.dimen.mainFragmentItemsSpacing);
            RecyclerView listView = rootView.findViewById(R.id.listView);
            RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), spanCount);
            listView.setAdapter(adapter);
            listView.setLayoutManager(manager);
            listView.addItemDecoration(new SpacingItemDecoration(spanCount, spacings, true));
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity activity = getActivity();
            String[] titles = context.getResources().getStringArray(R.array.toolbar_spinner_items);
            if (this.floor < 0 || this.floor >= titles.length) {
                activity.setTitle("");
            }
            activity.setTitle(titles[this.floor]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
