package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.PlayTreasureHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntContainer : BasicContainer(), PlayTreasureHuntView {

    companion object {
        val URI: String = PlayTreasureHuntContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.play_treasure_hunt_container

    private var playTreasureHuntPresenter: PlayTreasureHuntPresenter

    init {
        playTreasureHuntPresenter = if (PresenterManager.hasPresenter(PlayTreasureHuntPresenter.TAG))
            PresenterManager.getPresenter(PlayTreasureHuntPresenter.TAG) as PlayTreasureHuntPresenter
        else
            PresenterManager.addPresenter(PlayTreasureHuntPresenter.TAG, PlayTreasureHuntPresenter()) as PlayTreasureHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.play_treasure_hunt_action_bar_title))

        return this
    }

    override fun onPause() {
        super.onPause()

        playTreasureHuntPresenter.unsubscribe()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        playTreasureHuntPresenter.reload(this)
    }

    override fun onFinish() {
        super.onFinish()

        playTreasureHuntPresenter.finish()
    }
}