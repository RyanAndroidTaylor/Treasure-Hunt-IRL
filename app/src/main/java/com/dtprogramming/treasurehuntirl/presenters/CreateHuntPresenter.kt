package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import com.dtprogramming.treasurehuntirl.util.randomUuid

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter(val treasureHuntConnection: TreasureHuntConnection, val treasureChestConnection: TreasureChestConnection) : Presenter {

    var treasureHuntTitle = "New Treasure Hunt"

    lateinit var treasureHuntId: String
        private set

    private lateinit var createHuntView: CreateHuntView

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun create(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHuntId = randomUuid()

        treasureHuntConnection.insert(TreasureHunt(treasureHuntId, treasureHuntTitle))

        createHuntView.setTitle(treasureHuntTitle)
    }

    fun load(createHuntView: CreateHuntView, treasureHuntId: String) {
        this.createHuntView = createHuntView
        this.treasureHuntId = treasureHuntId

        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntId, { createHuntView.onTreasureChestsLoaded(it) })

        loadTreasureHunt()
    }

    fun reload(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntId, { createHuntView.onTreasureChestsLoaded(it) })

        loadTreasureHunt()
    }

    private fun loadTreasureHunt() {
        val treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntId)

        treasureHuntTitle = treasureHunt.title
        createHuntView.setTitle(treasureHuntTitle)
    }

    fun onTitleChanged(newTitle: String) {
        treasureHuntTitle = newTitle
    }

    fun finish() {
        treasureHuntConnection.update(TreasureHunt(treasureHuntId, treasureHuntTitle))

        PresenterManager.removePresenter(TAG)
    }
}