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

    lateinit var treasureHuntId: String
        private set
    lateinit var treasureChestId: String
        private set

    private var initialTreasureChest = false

    fun create(treasureHuntId: String, createTreasureChestView: CreateTreasureChestView, initialTreasureChest: Boolean = false) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureHuntId = treasureHuntId
        this.initialTreasureChest = initialTreasureChest

        treasureChestId = randomUuid()

        treasureChestConnection.insert(TreasureChest(treasureChestId, treasureHuntId, treasureChestTitle, initialTreasureChest))

        createTreasureChestView.setTitle(treasureChestTitle)
    }

    fun load(treasureChestId: String, treasureHuntId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureChestId = treasureChestId
        this.treasureHuntId = treasureHuntId


        loadTreasureChest()
        loadClue()
        loadWaypoint()
    }

    fun reload(createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView

        createTreasureChestView.setTitle(treasureChestTitle)

        loadClue()
        loadWaypoint()
    }

    override fun unsubscribe() {
        treasureChestConnection.unsubscribe()
        clueConnection.unsubscribe()
        waypointConnection.unsubscribe()

        createTreasureChestView = null
    }

    override fun dispose() {
        treasureChestConnection.update(TreasureChest(treasureChestId, treasureHuntId, treasureChestTitle, initialTreasureChest))

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureChest() {
        val treasureChest = treasureChestConnection.getTreasureChest(treasureChestId)

        treasureChestTitle = treasureChest.title
        createTreasureChestView?.setTitle(treasureChestTitle)
    }

    private fun loadClue() {
        val clue = clueConnection.getTextClueForParent(treasureChestId)

        clue?.let { createTreasureChestView?.displayClue(it) }
    }

    private fun loadWaypoint() {
        val waypoint = waypointConnection.getWaypointForParent(treasureChestId)

        if (waypoint != null)
            createTreasureChestView?.loadMap()
    }

    fun titleChanged(newTitle: String) {
        treasureChestTitle = newTitle
    }

    fun mapLoaded() {
        val waypoint = waypointConnection.getWaypointForParent(treasureChestId)

        waypoint?.let { createTreasureChestView?.displayWaypoint(it) }
    }
}