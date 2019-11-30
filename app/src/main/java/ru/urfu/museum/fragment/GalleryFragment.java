package ru.urfu.museum.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.listeners.OnDismissListener;
import com.stfalcon.imageviewer.loader.ImageLoader;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;

public class GalleryFragment extends Fragment {

    private View rootView;
    private int entryId = -1;
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        Bundle bundle = getArguments();
        this.entryId = bundle != null ? bundle.getInt(KeyWords.ID, -1) : -1;
        this.position = bundle != null ? bundle.getInt(KeyWords.POSITION, 0) : 0;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null && getActivity() != null) {
            rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
            final Activity activity = getActivity();
            new StfalconImageViewer.Builder<>(activity, MocksProvider.getEntryImages(activity, this.entryId), new ImageLoader<Integer>() {

                public void loadImage(ImageView imageView, Integer imageUrl) {
                    imageView.setImageResource(imageUrl);
                }

            })
                    .withStartPosition(this.position)
                    .withBackgroundColor(activity.getResources().getColor(R.color.black))
                    .allowZooming(true)
                    .withDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            activity.finish();
                        }
                    })
                    .show();
        }
        return rootView;
    }
}
