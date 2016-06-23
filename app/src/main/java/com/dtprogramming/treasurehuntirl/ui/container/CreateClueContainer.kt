package com.dtprogramming.treasurehuntirl.ui.container

import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import kotlinx.android.synthetic.main.container_create_clue.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateClueContainer(val createHuntPresenter: CreateHuntPresenter) : BasicContainer() {

    override fun inflate(parent: ViewGroup): Container {
        return super.inflate(parent, R.layout.container_create_clue)
    }

    override fun loadViews(parent: ViewGroup) {
        parent.create_clue_container_save.setOnClickListener {
            createHuntPresenter.saveClue(parent.create_clue__container_clue_text.text.toString())
            createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
        }
    }

    override fun onBackPressed(): Boolean {
        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)

        return true
    }
}