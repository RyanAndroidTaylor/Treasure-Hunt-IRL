package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.*
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter(val playingTreasureHuntConnection: PlayingTreasureHuntConnection, val connectedClueConnection: CollectedClueConnection, val clueConnection: ClueConnection) : Presenter {

    private var playTreasureHuntView: PlayTreasureHuntView? = null

    lateinit var playingTreasureHuntUuid: String
        private set

    companion object {
        val TAG: String = PlayTreasureHuntPresenter::class.java.simpleName
    }

    fun start(playTreasureHuntView: PlayTreasureHuntView, treasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = treasureHuntUuid

        playingTreasureHuntConnection.insert(PlayingTreasureHunt(treasureHuntUuid))
        saveInitialClueToCollectedClues()

        loadData()
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, playingTreasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        loadData()
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView

        connectedClueConnection.subscribeToCollectedCluesForParentAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
    }

    private fun loadData() {
        connectedClueConnection.subscribeToCollectedCluesForParentAsync(playingTreasureHuntUuid, { playTreasureHuntView?.updateInventoryList(it) })
    }

    override fun unsubscribe() {
        playTreasureHuntView = null

        playingTreasureHuntConnection.unsubscribe()
        clueConnection.unsubscribe()
        connectedClueConnection.unsubscribe()
    }

    override fun dispose() {
        PresenterManager.removePresenter(TAG)
    }

    private fun saveInitialClueToCollectedClues() {
        val clue = clueConnection.getClueForParent(playingTreasureHuntUuid)

        clue?.let {
            connectedClueConnection.insert(CollectedClue(clue.uuid, playingTreasureHuntUuid))
        }
    }
}