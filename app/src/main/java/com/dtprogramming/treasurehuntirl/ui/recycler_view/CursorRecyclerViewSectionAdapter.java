/*
 * Copyright (C) 2014 skyfish.jy@gmail.com
 *
 * Copyright (C) 2015 thewaronryan@gmail.com
 * Added code to handle sections
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.dtprogramming.treasurehuntirl.ui.recycler_view;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class CursorRecyclerViewSectionAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerViewSectionAdapter<VH, Cursor> {

    private Cursor cursor;

    private boolean dataValid;

    private int rowIdColumn;

    private DataSetObserver dataSetObserver;

    public CursorRecyclerViewSectionAdapter(Cursor cursor) {
        this.cursor = cursor;
        dataValid = cursor != null;
        rowIdColumn = dataValid ? this.cursor.getColumnIndex("_id") : -1;
        dataSetObserver = new NotifyingDataSetObserver();
        if (this.cursor != null)
            this.cursor.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position))
            return cursor.getLong(rowIdColumn);

        return RecyclerView.NO_ID;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (!dataValid)
            throw new IllegalStateException("This should only be called when the cursor is valid");

        if (position < 0 || position > viewTypes.length)
            throw new IllegalStateException("No view type found for position " + position);

        if (isFooter(position)) {
            onBindFooter(holder);
        } else if (viewTypes[position] == DEFAULT_VIEW_TYPE) {
            cursor.moveToPosition(getAdjustedPositionForSections(position));
            onBindViewHolder(holder, cursor);
        } else if (viewTypes[position] == SECTION_VIEW_TYPE) {
            cursor.moveToPosition(getAdjustedPositionForSections(position));
            onBindSectionViewHolder(holder, cursor);
        } else {
            throw new IllegalStateException("No view type found for position " + position);
        }
    }

    @Override
    public void setViewTypes() {
        List<Integer> sectionPositions = new ArrayList<>();

        if (cursor.moveToFirst()) {
            if (needsSectionBefore(cursor))
                sectionPositions.add(cursor.getPosition());

            while (cursor.moveToNext()) {
                if (needsSectionBefore(cursor))
                    sectionPositions.add(cursor.getPosition());
            }
        }

        viewTypes = new int[cursor.getCount() + sectionPositions.size()];
        Arrays.fill(viewTypes, DEFAULT_VIEW_TYPE);

        int sectionOffset = 0;
        for (int i = 0; i < sectionPositions.size(); i++) {
            viewTypes[sectionPositions.get(i) + sectionOffset] = SECTION_VIEW_TYPE;
            sectionOffset++;
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null)
            old.close();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor)
            return null;

        final Cursor oldCursor = cursor;
        if (oldCursor != null && dataSetObserver != null)
            oldCursor.unregisterDataSetObserver(dataSetObserver);

        cursor = newCursor;
        if (cursor != null) {
            setViewTypes();

            cursor.registerDataSetObserver(dataSetObserver);

            rowIdColumn = newCursor.getColumnIndex("_id");
            dataValid = true;
            notifyDataSetChanged();
        } else {
            rowIdColumn = -1;
            dataValid = false;
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            dataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataValid = false;
            notifyDataSetChanged();
        }
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);
}
