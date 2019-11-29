package ru.urfu.museum.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.santalu.aspectratioimageview.AspectRatioImageView;

import java.util.List;

import ru.urfu.museum.R;
import ru.urfu.museum.activity.DetailActivity;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.utils.TypefaceManager;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Activity context;
    private List<Entry> entries;
    private LayoutInflater inflater;

    public MainAdapter(Activity context, List<Entry> entries) {
        this.context = context;
        this.entries = entries;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AspectRatioImageView image;
        private TextView title;
        private TextView author;
        private TextView text;
        private Button button;

        ViewHolder(View holderView) {
            super(holderView);
            image = holderView.findViewById(R.id.entryCardImage);
            title = holderView.findViewById(R.id.entryCardTitle);
            author = holderView.findViewById(R.id.entryCardAuthor);
            text = holderView.findViewById(R.id.entryCardText);
            button = holderView.findViewById(R.id.entryCardReadMore);

            title.setTypeface(TypefaceManager.getTypeface(context, TypefaceManager.MEDIUM));
        }

    }

    @Override
    public int getItemCount() {
        return (entries == null) ? 0 : entries.size();
    }

    @Override
    public long getItemId(int position) {
        return entries.get(position).id;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.entry_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Entry entry = entries.get(position);
        if (entry.image != -1) {
            viewHolder.image.setImageResource(entry.image);
        }
        viewHolder.title.setText(entry.title);
        viewHolder.author.setText(entry.author);
        viewHolder.text.setText(entry.text);
        viewHolder.button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (context != null) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(KeyWords.ID, Integer.toString(entry.id));
                    context.startActivity(intent);
                }
            }

        });
    }
}