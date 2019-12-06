package ru.urfu.museum.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.santalu.aspectratioimageview.AspectRatioImageView;

import java.util.ArrayList;

import ru.urfu.museum.R;
import ru.urfu.museum.activity.GalleryActivity;
import ru.urfu.museum.adapters.DetailGalleryAdapter;
import ru.urfu.museum.classes.DimensionsUtil;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.classes.SpacingItemDecoration;
import ru.urfu.museum.interfaces.DetailPageEntryListener;
import ru.urfu.museum.interfaces.DetailPageScrollListener;
import ru.urfu.museum.utils.DelayedTask;

public class DetailFragment extends Fragment {

    private View rootView;
    private Entry entry;
    private AspectRatioImageView image;
    private TextView title;
    private TextView author;
    private TextView text;
    private TextView galleryTitle;
    private RecyclerView gallery;
    private LinearLayout pagination;
    private RelativeLayout prev;
    private RelativeLayout next;
    private TextView prevSubtitle;
    private TextView nextSubtitle;
    private FloatingActionButton favorite;
    private DetailPageScrollListener onScrollListener;
    private DetailPageEntryListener onDisplayEntryListener;
    private DelayedTask hideFavoriteTask;
    private DelayedTask showFavoriteTask;

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
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            this.image = this.rootView.findViewById(R.id.detailImage);
            this.title = this.rootView.findViewById(R.id.detailTitle);
            this.author = this.rootView.findViewById(R.id.detailAuthor);
            this.text = this.rootView.findViewById(R.id.detailText);
            this.galleryTitle = this.rootView.findViewById(R.id.detailGalleryTitle);
            this.gallery = this.rootView.findViewById(R.id.detailGallery);
            this.favorite = this.rootView.findViewById(R.id.detailFavorite);
            this.pagination = this.rootView.findViewById(R.id.detailPagination);
            this.prev = this.rootView.findViewById(R.id.detailPrev);
            this.next = this.rootView.findViewById(R.id.detailNext);
            this.prevSubtitle = this.rootView.findViewById(R.id.detailPrevSubtitle);
            this.nextSubtitle = this.rootView.findViewById(R.id.detailNextSubtitle);
            this.onBindView();
        }
        return this.rootView;
    }

    public void setScrollListener(DetailPageScrollListener listener) {
        this.onScrollListener = listener;
    }

    public void setOnDisplayEntryListener(DetailPageEntryListener listener) {
        this.onDisplayEntryListener = listener;
    }

    private void handleScroll(int scrollY) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null || activity.getSupportActionBar() == null) {
            return;
        }
        float toolbarHeight = activity.getSupportActionBar().getHeight();
        float totalHeight = this.image.getHeight();
        float percentage = Math.max(0, Math.min(100.0f, (scrollY / (totalHeight - toolbarHeight - getStatusBarHeight())) * 100));
        this.onScrollListener.onScrollProgress(percentage);
        this.onScrollListener.onSetTitle(percentage > 99.99f ? this.entry.title : "");
        this.handleFavoriteOnScroll(percentage);
    }

    private void handleFavoriteOnScroll(float scrollPercentage) {
        Activity activity = getActivity();
        if (activity == null || this.favorite == null || this.showFavoriteTask == null || this.hideFavoriteTask == null) {
            return;
        }
        if (scrollPercentage > 0 && this.favorite.getVisibility() == View.VISIBLE) {
            if (this.hideFavoriteTask.isComplete()) {
                this.hideFavoriteTask.start();
            }
            if (!this.showFavoriteTask.isComplete()) {
                this.showFavoriteTask.stop();
            }
        }
        if (scrollPercentage == 0 && this.favorite.getVisibility() == View.GONE) {
            if (this.showFavoriteTask.isComplete()) {
                this.favorite.show();
                this.showFavoriteTask.start();
            }
            if (!this.hideFavoriteTask.isComplete()) {
                this.hideFavoriteTask.stop();
            }
        }
    }

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void onBindView() {
        if (this.entry == null || getActivity() == null) {
            return;
        }
        if (this.entry.image != -1) {
            this.image.setImageResource(this.entry.image);
        }
        this.title.setText(this.entry.title);
        this.author.setText(this.entry.author);
        this.text.setText(this.entry.text);

        final ScrollView scrollView = rootView.findViewById(R.id.detailView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                if (onScrollListener == null) {
                    return;
                }
                handleScroll(scrollView.getScrollY());
            }

        });

        this.bindImages();
        this.bindFavorite();
        this.bindPagination();
    }

    private void bindImages() {
        if (this.entry == null || getActivity() == null) {
            return;
        }
        final Activity activity = getActivity();
        if (this.entry.image != -1) {
            this.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, GalleryActivity.class);
                    intent.putIntegerArrayListExtra(KeyWords.IMAGES, new ArrayList<Integer>() {{
                        add(entry.image);
                    }});
                    activity.startActivity(intent);
                }
            });
        }

        if (this.entry.gallery != null && this.entry.gallery.length > 0) {
            int spanCount = 3;
            float spacings = DimensionsUtil.pxFromDpResource(activity, R.dimen.detailGallerySpacing);
            DetailGalleryAdapter adapter = new DetailGalleryAdapter(activity, this.entry.id);
            RecyclerView.LayoutManager manager = new GridLayoutManager(activity, spanCount);
            this.gallery.setAdapter(adapter);
            this.gallery.setLayoutManager(manager);
            this.gallery.addItemDecoration(new SpacingItemDecoration(spanCount, spacings));
        } else {
            this.galleryTitle.setVisibility(View.GONE);
            this.gallery.setVisibility(View.GONE);
        }
    }

    private void bindPagination() {
        final Entry prevEntry = MocksProvider.getPrevEntry(getActivity(), this.entry.id);
        final Entry nextEntry = MocksProvider.getNextEntry(getActivity(), this.entry.id);
        if (prevEntry == null && nextEntry == null) {
            this.pagination.setVisibility(View.GONE);
        } else if (prevEntry == null) {
            this.prev.setVisibility(View.GONE);
            this.next.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onDisplayEntryListener.onDisplayEntry(nextEntry.id);
                }

            });
            this.nextSubtitle.setText(nextEntry.title);
        } else {
            this.next.setVisibility(View.GONE);
            this.prev.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onDisplayEntryListener.onDisplayEntry(prevEntry.id);
                }

            });
            this.prevSubtitle.setText(prevEntry.title);
        }
    }

    private void bindFavorite() {
        final Activity activity = getActivity();
        this.favorite.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                setFavoriteTopMargin();
            }

        });
        this.hideFavoriteTask = new DelayedTask(activity, R.string.timeout_fastest) {

            @Override
            public void execute() {
                favorite.hide();
            }

        };
        this.showFavoriteTask = new DelayedTask(activity, R.string.timeout_fastest) {

            @Override
            public void execute() {
            }

        };
    }

    private void setFavoriteTopMargin() {
        int imageHeight = this.image.getHeight();
        int buttonHeight = this.favorite.getHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.favorite.getLayoutParams();
        params.setMargins(params.leftMargin, imageHeight - (buttonHeight / 2), params.rightMargin, params.bottomMargin);
        this.favorite.setLayoutParams(params);
        this.favorite.requestLayout();
    }
}
