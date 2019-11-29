package ru.urfu.museum.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.santalu.aspectratioimageview.AspectRatioImageView;

import ru.urfu.museum.R;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;
import ru.urfu.museum.utils.TypefaceManager;

public class DetailFragment extends Fragment {

    private View rootView;
    private Entry entry;
    private AspectRatioImageView image;
    private TextView title;
    private TextView author;
    private TextView text;

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
            title.setTypeface(TypefaceManager.getTypeface(getActivity(), TypefaceManager.MEDIUM));
            this.onBindView();
        }
        return rootView;
    }

    private void onBindView() {
        if (entry == null) {
            return;
        }
        if (entry.image != -1) {
            image.setImageResource(entry.image);
        }
        title.setText(entry.title);
        author.setText(entry.author);
        text.setText(entry.text);
    }
}
