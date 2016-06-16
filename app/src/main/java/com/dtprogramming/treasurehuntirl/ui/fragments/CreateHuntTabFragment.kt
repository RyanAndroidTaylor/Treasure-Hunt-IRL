package com.dtprogramming.treasurehuntirl.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import kotlinx.android.synthetic.main.fragment_create_hunt.view.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntTabFragment : TabFragment() {
    override val title = "CREATE"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_create_hunt, container, false)

        view?.create_hunt_fragment_fab?.setOnClickListener { startActivity(CreateHuntActivity.getIntent(activity)) }

        return view
    }
}