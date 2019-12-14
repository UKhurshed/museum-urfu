package ru.urfu.museum.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santalu.aspectratioimageview.AspectRatioImageView;

import java.util.List;

import ru.urfu.museum.R;
import ru.urfu.museum.activity.DetailActivity;
import ru.urfu.museum.classes.Entry;
import ru.urfu.museum.classes.KeyWords;
import ru.urfu.museum.interfaces.SwitchFloorListener;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<Entry> entries;
    private int floor;
    private LayoutInflater inflater;
    public static final int TYPE_ENTRY = 0x0;
    public static final int TYPE_PAGE_PREV = 0x1;
    public static final int TYPE_PAGE_NEXT = 0x2;
    public static final int TYPE_BOTTOM_SPACER = 0x3;
    private SwitchFloorListener listener = null;

    public MainAdapter(Activity context, List<Entry> entries, int floor) {
        this.context = context;
        this.entries = entries;
        this.floor = floor;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {
        AspectRatioImageView image;
        TextView title;
        TextView author;
        TextView text;
        Button button;

        EntryViewHolder(View holderView) {
            super(holderView);
            image = holderView.findViewById(R.id.entryCardImage);
            title = holderView.findViewById(R.id.entryCardTitle);
            author = holderView.findViewById(R.id.entryCardAuthor);
            text = holderView.findViewById(R.id.entryCardText);
            button = holderView.findViewById(R.id.entryCardReadMore);
        }
    }

    class FloorViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent;

        FloorViewHolder(View holderView, String name) {
            super(holderView);
            parent = holderView.findViewById(R.id.mainFloorSwitcher);
            TextView floorName = holderView.findViewById(R.id.mainFloorSwitcherName);
            floorName.setText(name);
        }
    }

    class SpacerViewHolder extends RecyclerView.ViewHolder {
        SpacerViewHolder(View holderView) {
            super(holderView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (floor == 1 && position == this.getItemCount() - 2) {
            return MainAdapter.TYPE_PAGE_NEXT;
        }
        if (floor == 2 && position == 0) {
            return MainAdapter.TYPE_PAGE_PREV;
        }
        if (position == this.getItemCount() - 1) {
            return MainAdapter.TYPE_BOTTOM_SPACER;
        }
        return MainAdapter.TYPE_ENTRY;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (this.entries != null) {
            count += this.entries.size();
        }
        count++; // Next floor (on First floor), Prev floor (on Second floor)
        count++; // Bottom spacer view
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PAGE_NEXT:
            case TYPE_PAGE_PREV:
                View floorView = inflater.inflate(R.layout.main_floor_switcher, parent, false);
                String floorName = context.getResources().getString(
                        viewType == TYPE_PAGE_NEXT ? R.string.second_floor : R.string.first_floor
                );
                return new FloorViewHolder(floorView, floorName);
            case TYPE_ENTRY:
                View entryView = inflater.inflate(R.layout.entry_card, parent, false);
                return new EntryViewHolder(entryView);
            case TYPE_BOTTOM_SPACER:
            default:
                View spacerView = inflater.inflate(R.layout.main_spacer, parent, false);
                return new SpacerViewHolder(spacerView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        switch (this.getItemViewType(position)) {
            case TYPE_PAGE_PREV:
                final FloorViewHolder prevViewHolder = (FloorViewHolder) viewHolder;
                prevViewHolder.parent.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onDisplayFloor(floor - 1);
                        }
                    }

                });
                break;
            case TYPE_PAGE_NEXT:
                final FloorViewHolder nextViewHolder = (FloorViewHolder) viewHolder;
                nextViewHolder.parent.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onDisplayFloor(floor + 1);
                        }
                    }

                });
                break;
            case TYPE_ENTRY:
                final EntryViewHolder entryViewHolder = (EntryViewHolder) viewHolder;
                final Entry entry = this.getEntryByPosition(position);
                if (entry.image != -1) {
                    entryViewHolder.image.setImageResource(entry.image);
                }
                entryViewHolder.title.setText(entry.title);
                entryViewHolder.author.setText(entry.author);
                entryViewHolder.text.setText(entry.text);
                entryViewHolder.button.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (context != null) {
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(KeyWords.ID, Integer.toString(entry.id));
                            context.startActivity(intent);
                        }
                    }

                });
            default:
                break;
        }
    }

    public void setOnSwitchFloorListener(SwitchFloorListener listener) {
        this.listener = listener;
    }

    private Entry getEntryByPosition(int position) {
        if (floor == 2) {
            position--;
        }
        return this.entries.get(position);
    }
}