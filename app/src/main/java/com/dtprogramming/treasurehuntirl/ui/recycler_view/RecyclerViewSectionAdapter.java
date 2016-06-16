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

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

public abstract class RecyclerViewSectionAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> implements SectionAdapter<VH, T>, SectionHandler<VH, T>, FooterHandler<VH>, RecyclerView.OnItemTouchListener {

    protected int FOOTER_COUNT = 1;
    protected int NO_ITEMS = 0;

    protected int DEFAULT_VIEW_TYPE = 0;
    protected int FOOTER_VIEW_TYPE = 2;
    protected int SECTION_VIEW_TYPE = 3;

    protected int[] viewTypes;

    protected boolean footerEnabled;

    @Override
    public int getItemCount() {
        if (viewTypes != null) {
            if (footerEnabled)
                return viewTypes.length + FOOTER_COUNT;

            return viewTypes.length;
        }

        return NO_ITEMS;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position))
            return FOOTER_VIEW_TYPE;

        return viewTypes[position];
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW_TYPE)
            return onCreateFooter(parent);
        else if (viewType == SECTION_VIEW_TYPE)
            return onCreateSectionViewHolder(parent);

        return onCreateViewHolder(parent);
    }

    public abstract VH onCreateViewHolder(ViewGroup parent);

    public boolean isNormalView(int position) {
        return getItemViewType(position) == DEFAULT_VIEW_TYPE;
    }

    public boolean isFooter(int position) {
        return position == viewTypes.length && footerEnabled;
    }

    public int getAdjustedPositionForSections(int position) {
        int sections = 0;

        for (int i = 0; i < position; i++) {
            if (viewTypes[i] == SECTION_VIEW_TYPE)
                sections++;
        }

        return position - sections;
    }

    public void enableFooter() {
        footerEnabled = true;
    }

    @Override
    public VH onCreateSectionViewHolder(ViewGroup parent) {
        Log.e("RecyclerSectionAdapter", "onCreateSectionViewHolder was never overridden");

        return null;
    }

    @Override
    public void onBindSectionViewHolder(VH viewHolder, T item) {
        Log.e("RecyclerSectionAdapter", "onBindSectionViewHolder was never overridden");
    }

    @Override
    public VH onCreateFooter(ViewGroup parent) {
        Log.e("RecyclerSectionAdapter", "onCreateFooter was never overridden");

        return null;
    }

    @Override
    public void onBindFooter(VH viewHolder) {
        Log.e("RecyclerSectionAdapter", "onBindFooter was never overridden");
    }
}
