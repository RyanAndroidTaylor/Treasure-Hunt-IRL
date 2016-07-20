package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter : Presenter {

    private var playTreasureHuntView: PlayTreasureHuntView? = null

    companion object {
        val TAG: String = PlayTreasureHuntPresenter::class.java.simpleName
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, treasureHuntId: String) {
        this.playTreasureHuntView = playTreasureHuntView
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView
    }

    override fun unsubscribe() {
        playTreasureHuntView = null
    }

    override fun finish() {
        PresenterManager.removePresenter(TAG)
    }
}