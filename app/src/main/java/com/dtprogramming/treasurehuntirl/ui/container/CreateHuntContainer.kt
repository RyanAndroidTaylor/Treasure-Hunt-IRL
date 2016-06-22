package com.dtprogramming.treasurehuntirl.ui.container

import android.support.v7.widget.LinearLayoutManager
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

    override fun inflate(parent: ViewGroup): Container {
        return super.inflate(parent, R.layout.container_create_hunt)
    }

    override fun loadViews(parent: ViewGroup) {
        parent.create_hunt_container_clue_list.layoutManager = CustomLinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)

//        for (i in 0..100)
//            clues.add("This is a clue to a sweet treasure hunt that only a few people will ever figure out. That is how good it is. We need a lot more text in here so it looks good to the eye when we are playing with in on the screen")

        adapter = ClueAdapter(parent.context, clues)

        parent.create_hunt_container_clue_list.adapter = adapter

        parent.create_hunt_add_clue.setOnClickListener {
            createHuntPresenter.switchState(CreateHuntPresenter.CREATE_CLUE)
        }

        parent.create_hunt_container_clue_list.addOnScrollListener(ClueScrollListener())
    }

    fun updateClueList(clues: List<String>) {
        adapter.updateList(clues)
    }
}