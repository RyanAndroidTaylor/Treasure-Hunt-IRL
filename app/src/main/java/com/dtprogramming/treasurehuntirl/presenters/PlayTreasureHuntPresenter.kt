package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.*
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView
import com.dtprogramming.treasurehuntirl.util.BURIED
import com.dtprogramming.treasurehuntirl.util.BURIED_LOCKED
import com.dtprogramming.treasurehuntirl.util.CLOSED
import com.dtprogramming.treasurehuntirl.util.LOCKED
import javax.inject.Inject

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter() : Presenter {

    @Inject
    lateinit var playingTreasureHuntConnection: PlayingTreasureHuntConnection
    @Inject
    lateinit var inventoryConnection: InventoryConnection
    @Inject
    lateinit var treasureChestConnection: TreasureChestConnection
    @Inject
    lateinit var collectedTreasureChestConnection: CollectedTreasureChestConnection

    private var playTreasureHuntView: PlayTreasureHuntView? = null

    lateinit var playingTreasureHuntUuid: String
        private set

    private var currentTreasureChest: TreasureChest? = null

    companion object {
        val TAG: String = PlayTreasureHuntPresenter::class.java.simpleName
    }

    init {
        THApp.databaseComponent.inject(this)
    }

    fun start(playTreasureHuntView: PlayTreasureHuntView, treasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = treasureHuntUuid

        playingTreasureHuntConnection.insert(PlayingTreasureHunt(treasureHuntUuid))
        collectAndOpenInitialTreasureChest()
        loadCurrentTreasureChest()
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, playingTreasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        inventoryConnection.getCollectedItemsForTreasureHuntAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
        loadCurrentTreasureChest()
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView

        inventoryConnection.getCollectedItemsForTreasureHuntAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
        loadCurrentTreasureChest()
    }

    fun performTreasureChestAction() {
        if (currentTreasureChest?.state == BURIED || currentTreasureChest?.state == BURIED_LOCKED) {
            playTreasureHuntView?.switchToDigMode()
        } else if (currentTreasureChest?.state == LOCKED) {
            currentTreasureChest?.let { playTreasureHuntView?.viewCollectedTreasureChest(it.uuid) }
        }
    }

    private fun loadCurrentTreasureChest() {
        val currentTreasureChest = treasureChestConnection.getCurrentTreasureChest(playingTreasureHuntUuid)

        if (currentTreasureChest == null) {
            playTreasureHuntView?.hideTreasureChestAction()
        } else if (currentTreasureChest.state == LOCKED) {
            playTreasureHuntView?.displayLockedTreasureChestAction()

            if (!collectedTreasureChestConnection.treasureChestIsCollected(currentTreasureChest.uuid))
                collectTreasureChest(currentTreasureChest)
        } else {
            playTreasureHuntView?.displayBuriedTreasureChestAction()
        }

        this.currentTreasureChest = currentTreasureChest
    }

    private fun collectTreasureChest(treasureChest: TreasureChest): CollectedTreasureChest {
        val collectedTreasureChest = treasureChest.collectTreasureChest()

        collectedTreasureChestConnection.insert(collectedTreasureChest)

        return collectedTreasureChest
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

    private fun collectAndOpenInitialTreasureChest() {
        val treasureChest = treasureChestConnection.getInitialTreasureChest(playingTreasureHuntUuid)

        val collectedTreasureChest = CollectedTreasureChest(treasureChest.uuid, treasureChest.title, playingTreasureHuntUuid, CLOSED)

        collectedTreasureChestConnection.insert(collectedTreasureChest)

        collectedTreasureChestConnection.openCollectedTreasureChest(collectedTreasureChest, { playTreasureHuntView?.updateInventoryList(it) })
    }
}