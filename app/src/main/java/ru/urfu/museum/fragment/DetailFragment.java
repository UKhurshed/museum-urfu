package ru.urfu.museum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santalu.aspectratioimageview.AspectRatioImageView;

import ru.urfu.museum.R;
import ru.urfu.museum.adapters.DetailGalleryAdapter;
import ru.urfu.museum.classes.DimensionsUtil;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.classes.SpacingItemDecoration;
import ru.urfu.museum.interfaces.DetailPageScrollListener;
import ru.urfu.museum.utils.TypefaceManager;

public class DetailFragment extends Fragment {

    private View rootView;
    private Entry entry;
    private AspectRatioImageView image;
    private TextView title;
    private TextView author;
    private TextView text;
    private TextView galleryTitle;
    private RecyclerView gallery;
    private DetailPageScrollListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        Bundle bundle = getArguments();
        int id = bundle != null ? bundle.getInt(KeyWords.ID, -1) : -1;
        this.entry = MocksProvider.getEntry(getActivity(), id);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            image = rootView.findViewById(R.id.detailImage);
            title = rootView.findViewById(R.id.detailTitle);
            author = rootView.findViewById(R.id.detailAuthor);
            text = rootView.findViewById(R.id.detailText);
            galleryTitle = rootView.findViewById(R.id.detailGalleryTitle);
            gallery = rootView.findViewById(R.id.detailGallery);
            this.onBindView();
        }
        return rootView;
    }

    public void setScrollListener(DetailPageScrollListener listener) {
        this.listener = listener;
    }

    private void handleScroll(int scrollY) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null || activity.getSupportActionBar() == null) {
            return;
        }
        float toolbarHeight = activity.getSupportActionBar().getHeight();
        float totalHeight = image.getHeight();
        float percentage = Math.max(0, Math.min(100.0f, (scrollY / (totalHeight - toolbarHeight - getStatusBarHeight())) * 100));
        listener.onScrollProgress(percentage);
        listener.onSetTitle(percentage > 99.99f ? entry.title : "");
    }

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void onBindView() {
        if (entry == null || getActivity() == null) {
            return;
        }
        if (entry.image != -1) {
            image.setImageResource(entry.image);
        }
        title.setText(entry.title);
        author.setText(entry.author);
        text.setText(entry.text);

        title.setTypeface(TypefaceManager.getTypeface(getActivity(), TypefaceManager.MEDIUM));
        galleryTitle.setTypeface(TypefaceManager.getTypeface(getActivity(), TypefaceManager.MEDIUM));

        final ScrollView scrollView = rootView.findViewById(R.id.detailView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                if (listener == null) {
                    return;
                }
                handleScroll(scrollView.getScrollY());
            }

        });

        if (entry.gallery != null && entry.gallery.length > 0) {
            int spanCount = 3;
            float spacings = DimensionsUtil.pxFromDpResource(getActivity(), R.dimen.detailGallerySpacing);
            DetailGalleryAdapter adapter = new DetailGalleryAdapter(getActivity(), entry.id);
            RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), spanCount);
            gallery.setAdapter(adapter);
            gallery.setLayoutManager(manager);
            gallery.addItemDecoration(new SpacingItemDecoration(spanCount, spacings));
        } else {
            galleryTitle.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
        }
    }
}
