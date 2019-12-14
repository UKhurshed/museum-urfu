package ru.urfu.museum.classes;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private GridLayoutManager.SpanSizeLookup mSpanLookup;

    public SpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    public SpacingItemDecoration(int spanCount, float spacing, boolean includeEdge) {
        this(spanCount, (int) spacing, includeEdge);
    }

    public SpacingItemDecoration(int spanCount, int spacing) {
        this(spanCount, spacing, false);
    }

    public SpacingItemDecoration(int spanCount, float spacing) {
        this(spanCount, (int) spacing, false);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        PositionOnLayout itemPosition = this.getPositionOnLayout(position);
        if (includeEdge) {
            outRect.left = itemPosition.column == 0 ? spacing : spacing / 2;
            outRect.right = itemPosition.isLatestColumn ? spacing : spacing / 2;
            if (itemPosition.row == 0) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        } else {
            int latestRow = this.getRowsCount(parent.getAdapter().getItemCount());
            outRect.left = 0;
            outRect.right = itemPosition.isLatestColumn ? 0 : spacing;
            if (itemPosition.row > 0 && itemPosition.row < latestRow) {
                outRect.top = spacing;
            }
        }
    }

    private int getSpanCount(int position) {
        if (this.mSpanLookup == null) {
            return 1;
        }
        return mSpanLookup.getSpanSize(position);
    }

    private int getRowsCount(int itemsCount) {
        PositionOnLayout latest = this.getPositionOnLayout(itemsCount);
        return latest.row;
    }

    private PositionOnLayout getPositionOnLayout(int position) {
        int row = 0;
        int column = -1;
        int usedSpans = 0;
        boolean latest = false;
        int exitCase = -1;
        for (int i = 0; i <= position; i++) {
            int spans = this.getSpanCount(i);
            if (usedSpans + spans == this.spanCount) {
                column++;
                usedSpans = this.spanCount;
                latest = true;
                exitCase = 1;
            } else if (usedSpans + spans > this.spanCount) {
                row++;
                column = 0;
                usedSpans = spans;
                latest = usedSpans == this.spanCount;
                exitCase = 2;
            } else {
                column++;
                usedSpans += spans;
                exitCase = 3;
            }
        }
        return new PositionOnLayout(row, column, latest, exitCase);
    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup lookup) {
        this.mSpanLookup = lookup;
    }

    private class PositionOnLayout {
        int row;
        int column;
        boolean isLatestColumn;
        int exitCase;

        PositionOnLayout(int row, int column, boolean isLatestColumn, int exitCase) {
            this.row = row;
            this.column = column;
            this.isLatestColumn = isLatestColumn;
            this.exitCase = exitCase;
        }
    }
}