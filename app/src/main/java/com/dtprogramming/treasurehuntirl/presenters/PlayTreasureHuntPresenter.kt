package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter(val clueConnection: ClueConnection) : Presenter {

    private var playTreasureHuntView: PlayTreasureHuntView? = null

    private lateinit var treasureHuntId: String

    companion object {
        val TAG: String = PlayTreasureHuntPresenter::class.java.simpleName
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, treasureHuntId: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.treasureHuntId = treasureHuntId

        clueConnection.subscribeToCollectedCluesForPlayingTreasureHuntAsync(treasureHuntId, { playTreasureHuntView.updateInventoryList(it) })
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView

        clueConnection.subscribeToCollectedCluesForPlayingTreasureHuntAsync(treasureHuntId, { playTreasureHuntView.updateInventoryList(it) })
    }

    override fun unsubscribe() {
        playTreasureHuntView = null

        clueConnection.unsubscribe()
    }

    override fun finish() {
        PresenterManager.removePresenter(TAG)
    }

    fun dig() {

    }
}