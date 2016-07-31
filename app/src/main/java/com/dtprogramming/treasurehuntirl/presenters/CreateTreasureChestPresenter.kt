package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.ui.views.CreateTreasureChestView
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

    private var initialTreasureChest = false

    fun create(treasureHuntId: String, createTreasureChestView: CreateTreasureChestView, initialTreasureChest: Boolean = false) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureHuntUuid = treasureHuntId
        this.initialTreasureChest = initialTreasureChest

        treasureChestUuid = randomUuid()

        treasureChestConnection.insert(TreasureChest(treasureChestUuid, treasureHuntId, treasureChestTitle, initialTreasureChest))

        createTreasureChestView.setTitle(treasureChestTitle)
    }

    fun load(treasureChestId: String, treasureHuntId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureChestUuid = treasureChestId
        this.treasureHuntUuid = treasureHuntId


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
        treasureChestConnection.update(TreasureChest(treasureChestUuid, treasureHuntUuid, treasureChestTitle, initialTreasureChest))

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureChest() {
        val treasureChest = treasureChestConnection.getTreasureChest(treasureChestUuid)

        treasureChestTitle = treasureChest.title
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