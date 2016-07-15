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

        requestTreasureChestsForTreasureHunt()
        loadTreasureHunt()
    }

    fun reload(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        requestTreasureChestsForTreasureHunt()
        loadTreasureHunt()
    }

    override fun unsubscribe() {
        treasureHuntConnection.unsubscribe()
        treasureChestConnection.unsubscribe()
    }

    override fun finish() {
        treasureHuntConnection.update(TreasureHunt(treasureHuntId, treasureHuntTitle))

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureHunt() {
        val treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntId)

        treasureHuntTitle = treasureHunt.title
        createHuntView.setTitle(treasureHuntTitle)
    }

    private fun requestTreasureChestsForTreasureHunt() {
        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntId, { createHuntView.onTreasureChestsLoaded(it) })
    }

    fun onTitleChanged(newTitle: String) {
        treasureHuntTitle = newTitle
    }
}