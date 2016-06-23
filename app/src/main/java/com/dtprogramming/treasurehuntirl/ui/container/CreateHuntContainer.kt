package com.dtprogramming.treasurehuntirl.ui.container

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueScrollListener
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CustomLinearLayoutManager
import kotlinx.android.synthetic.main.container_create_hunt.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntContainer(val createHuntPresenter: CreateHuntPresenter, val clues: List<String>) : BasicContainer() {

    private lateinit var adapter: ClueAdapter

    private lateinit var clueList: RecyclerView

    override lateinit var layout: View

    override fun inflate(parent: ViewGroup): Container {
        return super.inflate(parent, R.layout.container_create_hunt)
    }

    override fun loadViews(parent: ViewGroup) {
        clueList = parent.create_hunt_container_clue_list

        clueList.layoutManager = CustomLinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)

        adapter = ClueAdapter(parent.context, clues)

        clueList.adapter = adapter

        parent.create_hunt_container_add_clue.setOnClickListener {
            createHuntPresenter.switchState(CreateHuntPresenter.CREATE_CLUE)
        }

        parent.create_hunt_container_add_waypoint.setOnClickListener {
            createHuntPresenter.switchState(CreateHuntPresenter.CREATE_WAY_POINT)
        }

        parent.create_hunt_container_save.setOnClickListener {  }

        parent.create_hunt_container_cancel.setOnClickListener {  }

        clueList.addOnScrollListener(ClueScrollListener())
    }

    fun updateClueList(clues: List<String>) {
        adapter.updateList(clues)
    }
}