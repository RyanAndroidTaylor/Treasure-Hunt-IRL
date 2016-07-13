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

    private lateinit var createTreasureChestView: CreateTreasureChestView

    private var treasureChestTitle = "New Treasure Chest"

    lateinit var treasureHuntId: String
        private set
    lateinit var treasureChestId: String
        private set

    fun newTreasureChest(treasureHuntId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureHuntId = treasureHuntId

        treasureChestId = randomUuid()

        treasureChestConnection.insert(TreasureChest(treasureChestId, treasureHuntId, treasureChestTitle))
    }

    fun loadTreasureChest(treasureChestId: String, treasureHuntId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureChestId = treasureChestId
        this.treasureHuntId = treasureHuntId

        val treasureChest = treasureChestConnection.getTreasureChest(treasureChestId)

        treasureChestTitle = treasureChest.title
        createTreasureChestView.setTitle(treasureChestTitle)

        val clue = clueConnection.getClueForTreasureChest(treasureChestId)

        clue?.let { createTreasureChestView.displayClue(it) }

        val waypoint = waypointConnection.getWaypointForTreasureChest(treasureChestId)

        if (waypoint != null)
            createTreasureChestView.loadMap()
    }

    fun reload(createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView

        val clue = clueConnection.getClueForTreasureChest(treasureChestId)

        clue?.let { createTreasureChestView.displayClue(it) }

        createTreasureChestView.setTitle(treasureChestTitle)

        val waypoint = waypointConnection.getWaypointForTreasureChest(treasureChestId)

        if (waypoint != null)
            createTreasureChestView.loadMap()
    }

    fun titleChanged(newTitle: String) {
        treasureChestTitle = newTitle
    }

    fun mapLoaded() {
        val waypoint = waypointConnection.getWaypointForTreasureChest(treasureChestId)

        waypoint?.let { createTreasureChestView.displayWaypoint(it) }
    }

    fun finish() {
        treasureChestConnection.update(TreasureChest(treasureChestId, treasureHuntId, treasureChestTitle))

        PresenterManager.removePresenter(TAG)
    }
}