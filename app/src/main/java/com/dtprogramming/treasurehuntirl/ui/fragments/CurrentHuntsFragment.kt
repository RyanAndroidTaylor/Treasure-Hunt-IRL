package com.dtprogramming.treasurehuntirl.ui.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureHuntAdapter

/**
 * Created by ryantaylor on 6/15/16.
 */
class CurrentHuntsFragment : TabFragment() {
    override val title = "IN PROGRESS"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_current_hunts, container, false)

        return view
    }
}