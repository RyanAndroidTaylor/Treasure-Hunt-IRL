package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.*
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter(val playingTreasureHuntConnection: PlayingTreasureHuntConnection, val inventoryConnection: InventoryConnection, val treasureChestConnection: TreasureChestConnection, val collectedTreasureChestConnection: CollectedTreasureChestConnection) : Presenter {

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
        openAndCollectInitialTreasureChest()

        loadData()
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, playingTreasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        loadData()
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView

        inventoryConnection.getCollectedItemsForTreasureHuntAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
    }

    private fun loadData() {
        inventoryConnection.getCollectedItemsForTreasureHuntAsync(playingTreasureHuntUuid, { playTreasureHuntView?.updateInventoryList(it) })
    }

    override fun unsubscribe() {
        playTreasureHuntView = null

        playingTreasureHuntConnection.unsubscribe()
        inventoryConnection.unsubscribe()
    }

    override fun dispose() {
        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    private fun openAndCollectInitialTreasureChest() {
        val treasureChest = treasureChestConnection.getInitialTreasureChest(playingTreasureHuntUuid)

        collectedTreasureChestConnection.insert(CollectedTreasureChest(treasureChest.uuid, treasureChest.title, playingTreasureHuntUuid, CollectedTreasureChest.OPEN))
    }
}