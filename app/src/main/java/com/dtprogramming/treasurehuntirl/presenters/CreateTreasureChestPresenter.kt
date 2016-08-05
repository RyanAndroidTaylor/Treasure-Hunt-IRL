package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.ui.views.CreateTreasureChestView
import com.dtprogramming.treasurehuntirl.util.BURIED
import com.dtprogramming.treasurehuntirl.util.randomUuid

/**
 * Created by ryantaylor on 7/11/16.
 */
class CreateTreasureChestPresenter(val treasureChestConnection: TreasureChestConnection, val clueConnection: ClueConnection, val waypointConnection: WaypointConnection) : Presenter {

    companion object {
        val TAG: String = CreateTreasureChestPresenter::class.java.simpleName
    }

    private var createTreasureChestView: CreateTreasureChestView? = null

    private var treasureChestTitle = "New Treasure Chest"
        private set

    lateinit var treasureHuntUuid: String
        private set
    lateinit var treasureChestUuid: String
        private set

    private var treasureChestOrder = 0

    fun create(treasureHuntUuid: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureHuntUuid = treasureHuntUuid

        treasureChestUuid = randomUuid()
        treasureChestOrder = treasureChestConnection.getNextTreasureChestPosition(this.treasureHuntUuid)

        treasureChestConnection.insert(TreasureChest(treasureChestUuid, treasureHuntUuid, treasureChestTitle, treasureChestOrder, BURIED))

        createTreasureChestView.setTitle(treasureChestTitle)
    }

    fun load(treasureChestId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureChestUuid = treasureChestId


        loadTreasureChest()
        loadClues()
        loadWaypoint()
    }

    fun reload(createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView

        createTreasureChestView.setTitle(treasureChestTitle)

        loadClues()
        loadWaypoint()
    }

    override fun unsubscribe() {
        treasureChestConnection.unsubscribe()
        clueConnection.unsubscribe()
        waypointConnection.unsubscribe()

        createTreasureChestView = null
    }

    override fun dispose() {
        unsubscribe()

        treasureChestConnection.update(TreasureChest(treasureChestUuid, treasureHuntUuid, treasureChestTitle, treasureChestOrder, BURIED))

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureChest() {
        val treasureChest = treasureChestConnection.getTreasureChest(treasureChestUuid)

        treasureHuntUuid = treasureChest.treasureHuntUuid
        treasureChestTitle = treasureChest.title
        treasureChestOrder = treasureChest.order

        createTreasureChestView?.setTitle(treasureChestTitle)
    }

    private fun loadClues() {
        clueConnection.getCluesForParentAsync(treasureChestUuid, { createTreasureChestView?.updateClueList(it) })
    }

    private fun loadWaypoint() {
        val waypoint = waypointConnection.getWaypointForParent(treasureChestUuid)

        if (waypoint != null)
            createTreasureChestView?.loadMap()
    }

    fun titleChanged(newTitle: String) {
        treasureChestTitle = newTitle
    }

    fun mapLoaded() {
        val waypoint = waypointConnection.getWaypointForParent(treasureChestUuid)

        waypoint?.let { createTreasureChestView?.displayWaypoint(it) }
    }
}