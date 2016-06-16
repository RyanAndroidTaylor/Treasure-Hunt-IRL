package com.dtprogramming.treasurehuntirl.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R

/**
 * Created by ryantaylor on 6/15/16.
 */
class TreasureHuntListFragment : TabFragment() {
    override val title = "HUNTS"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_hunt_list, container, false)
    }
}