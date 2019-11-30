package ru.urfu.museum.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.santalu.aspectratioimageview.AspectRatioImageView;

import ru.urfu.museum.R;
import ru.urfu.museum.activity.GalleryActivity;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.classes.MocksProvider;

public class DetailGalleryAdapter extends RecyclerView.Adapter<DetailGalleryAdapter.ViewHolder> {

    private Activity context;
    private int[] images;
    private int entryId;
    private LayoutInflater inflater;

    public DetailGalleryAdapter(Activity context, int entryId) {
        Entry entry = MocksProvider.getEntry(context, entryId);
        this.context = context;
        this.images = entry != null && entry.gallery != null ? entry.gallery : new int[0];
        this.entryId = entryId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AspectRatioImageView image;

        ViewHolder(View holderView) {
            super(holderView);
            image = holderView.findViewById(R.id.detailGalleryImage);
        }

    }

    @Override
    public int getItemCount() {
        return (images == null) ? 0 : images.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public DetailGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.detail_gallery_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.image.setImageResource(this.images[position]);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (context != null) {
                    Intent intent = new Intent(context, GalleryActivity.class);
                    intent.putExtra(KeyWords.ID, Integer.toString(entryId));
                    intent.putExtra(KeyWords.POSITION, Integer.toString(position));
                    context.startActivity(intent);
                }
            }

        });
    }
}