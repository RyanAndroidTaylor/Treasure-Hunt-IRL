package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.CollectedClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.PlayingTreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter(val playingTreasureHuntConnection: PlayingTreasureHuntConnection, val connectedClueConnection: CollectedClueConnection, val clueConnection: ClueConnection) : Presenter {

    private var playTreasureHuntView: PlayTreasureHuntView? = null

    private lateinit var playingTreasureHuntUuid: String

    companion object {
        val TAG: String = PlayTreasureHuntPresenter::class.java.simpleName
    }

    fun start(playTreasureHuntView: PlayTreasureHuntView, treasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = treasureHuntUuid

        playingTreasureHuntConnection.insert(PlayingTreasureHunt(treasureHuntUuid))
        saveInitialClueToCollectedClues()

        connectedClueConnection.subscribeToCollectedCluesForParentAsync(treasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, playingTreasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        connectedClueConnection.subscribeToCollectedCluesForParentAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView

        connectedClueConnection.subscribeToCollectedCluesForParentAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
    }

    override fun unsubscribe() {
        playTreasureHuntView = null

        connectedClueConnection.unsubscribe()
    }

    override fun finish() {
        PresenterManager.removePresenter(TAG)
    }

    private fun saveInitialClueToCollectedClues() {
        val clue = clueConnection.getClueForParent(playingTreasureHuntUuid)

        clue?.let {
            connectedClueConnection.insert(CollectedClue(clue.uuid, playingTreasureHuntUuid))
        }
    }

    fun dig() {

    }
}