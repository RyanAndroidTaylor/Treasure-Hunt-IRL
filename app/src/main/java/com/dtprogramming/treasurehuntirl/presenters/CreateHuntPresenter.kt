package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import java.util.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter(val treasureHuntConnection: TreasureHuntConnection, val clueConnection: ClueConnection, val waypointConnection: WaypointConnection) : Presenter {

    var treasureHuntTitle = "New Treasure Hunt"
    lateinit var treasureHuntId: String
        private set

    private lateinit var createHuntView: CreateHuntView

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun load(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
    }

    fun finish() {
        PresenterManager.removePresenter(TAG)
    }
}