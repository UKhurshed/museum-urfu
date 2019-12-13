package ru.urfu.museum.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.urfu.museum.R;
import ru.urfu.museum.adapters.MainAdapter;
import ru.urfu.museum.classes.DimensionsUtil;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.classes.SpacingItemDecoration;
import ru.urfu.museum.interfaces.SwitchFloorListener;

public class MainFragment extends Fragment {

    private int floor = -1;
    private View rootView;
    private MainAdapter adapter;
    private SwitchFloorListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        Bundle bundle = getArguments();
        this.floor = bundle != null ? bundle.getInt(KeyWords.FLOOR, 1) : 1;
        adapter = new MainAdapter(getActivity(), MocksProvider.getEntries(getActivity(), floor), floor);
        adapter.setHasStableIds(true);
        if (this.listener != null) {
            adapter.setOnSwitchFloorListener(listener);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null && getActivity() != null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final int spanCount = Integer.parseInt(getActivity().getResources().getString(R.string.entries_cols));
            float spacings = DimensionsUtil.pxFromDpResource(getActivity(), R.dimen.mainFragmentItemsSpacing);
            RecyclerView listView = rootView.findViewById(R.id.listView);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), spanCount);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    switch (adapter.getItemViewType(position)) {
                        case MainAdapter.TYPE_BOTTOM_SPACER:
                        case MainAdapter.TYPE_PAGE_PREV:
                        case MainAdapter.TYPE_PAGE_NEXT:
                            return spanCount;
                        default:
                            return 1;
                    }
                }

            });
            listView.setAdapter(adapter);
            listView.setLayoutManager(manager);
            listView.addItemDecoration(new SpacingItemDecoration(spanCount, spacings, true));
        }
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Activity activity = getActivity();
            String[] titles = context.getResources().getStringArray(R.array.toolbar_spinner_items);
            if (this.floor < 0 || this.floor >= titles.length) {
                activity.setTitle("");
            } else {
                activity.setTitle(titles[this.floor]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnSwitchFloorListener(SwitchFloorListener listener) {
        this.listener = listener;
    }
}
