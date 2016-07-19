package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntContainer : BasicContainer() {

    companion object {
        val URI: String = PlayTreasureHuntContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.play_treasure_hunt_container

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.play_treasure_hunt_action_bar_title))

        return this
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)
    }

    override fun onFinish() {
        super.onFinish()
    }
}