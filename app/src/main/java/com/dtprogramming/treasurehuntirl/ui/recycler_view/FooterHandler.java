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
import android.view.ViewGroup;

public interface FooterHandler<VH extends RecyclerView.ViewHolder> {

    VH onCreateFooter(ViewGroup parent);

    /**
     * Use this method if you want to put something in the footer
     */
    void onBindFooter(VH viewHolder);
}