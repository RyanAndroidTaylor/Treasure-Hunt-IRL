package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import java.util.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter(val treasureHuntConnection: TreasureHuntConnection, val clueConnection: ClueConnection, val waypointConnection: WaypointConnection) : Presenter {

    private var isNewHunt = false
    var treasureHuntTitle = "New Treasure Hunt"
    lateinit var treasureHuntId: String
        private set

    private lateinit var createHuntView: CreateHuntView

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun createHunt(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHuntId = UUID.randomUUID().toString().replace("-", "")

        isNewHunt = true

        treasureHuntConnection.insert(TreasureHunt(treasureHuntId, treasureHuntTitle))

        createHuntView.setTitle(treasureHuntTitle)
    }

    fun loadHunt(treasureHuntId: String, createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        val treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntId)

        this.treasureHuntId = treasureHunt.uuid
        treasureHuntTitle = treasureHunt.title

        createHuntView.setTitle(treasureHuntTitle)

        clueConnection.getTreasureHuntCluesAsync(treasureHuntId, { createHuntView.updateClueList(it) })

        isNewHunt = false
    }

    fun reloadHunt(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        createHuntView.setTitle(treasureHuntTitle)

        clueConnection.getTreasureHuntCluesAsync(treasureHuntId, { createHuntView.updateClueList(it) })
    }

    fun mapLoaded() {
        waypointConnection.getTreasureHuntWaypointsAsync(treasureHuntId, { createHuntView.updateWaypoints(it) })
    }

    fun titleChange(title: String) {
        treasureHuntTitle = title
    }

    fun finish() {
        save()

        treasureHuntConnection.unsubscribe()
        clueConnection.unsubscribe()
        waypointConnection.unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    private fun save() {
        treasureHuntConnection.update(TreasureHunt(treasureHuntId, treasureHuntTitle))
    }
}