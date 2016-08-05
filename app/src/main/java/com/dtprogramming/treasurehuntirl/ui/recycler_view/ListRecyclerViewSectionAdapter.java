/*
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

import android.support.v7.widget.RecyclerView;

public abstract class ListRecyclerViewSectionAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerViewSectionAdapter<VH, T> implements SectionHandler<VH, T>, FooterHandler<VH> {

    protected List<T> items;

    public ListRecyclerViewSectionAdapter(List<T> items) {
        this.items = items;

        setViewTypes();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position < 0 || position > viewTypes.length)
            throw new IllegalStateException("No view type found for position " + position);

        if (isFooter(position)) {
            onBindFooter(holder);
        } else if (viewTypes[position] == DEFAULT_VIEW_TYPE) {
            onBindViewHolder(holder, items.get(getAdjustedPositionForSections(position)));
        } else if (viewTypes[position] == SECTION_VIEW_TYPE) {
            onBindSectionViewHolder(holder, items.get(getAdjustedPositionForSections(position)));
        } else {
            throw new IllegalStateException("No view type found for position " + position);
        }
    }

    @Override
    public void setViewTypes() {
        List<Integer> sectionPositions = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (needsSectionBefore(items.get(i)))
                sectionPositions.add(i);
        }

        viewTypes = new int[items.size() + sectionPositions.size()];
        Arrays.fill(viewTypes, DEFAULT_VIEW_TYPE);

        int sectionOffset = 0;
        for (int i = 0; i < sectionPositions.size(); i++) {
            viewTypes[sectionPositions.get(i) + sectionOffset] = SECTION_VIEW_TYPE;
            sectionOffset++;
        }
    }

    public void updateList(List<T> items) {
        this.items = items;
        setViewTypes();
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        items.add(item);

        notifyItemInserted(items.size() -1);
    }

    public abstract void onBindViewHolder(VH viewHolder, T item);
}
