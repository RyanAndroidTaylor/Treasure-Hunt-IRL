package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import com.dtprogramming.treasurehuntirl.util.randomUuid

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter(val treasureHuntConnection: TreasureHuntConnection, val treasureChestConnection: TreasureChestConnection, val clueConnection: ClueConnection) : Presenter {

    var treasureHuntTitle = "New Treasure Hunt"

    lateinit var treasureHuntUuid: String
        private set
    lateinit var initialTreasureChestUuid: String
        private set

    private var createHuntView: CreateHuntView? = null

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun create(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHuntUuid = randomUuid()

        treasureHuntConnection.insert(TreasureHunt(treasureHuntUuid, treasureHuntTitle))
        createInitialTreasureChest()

        createHuntView.setTitle(treasureHuntTitle)
    }

    fun load(createHuntView: CreateHuntView, treasureHuntUuid: String) {
        this.createHuntView = createHuntView
        this.treasureHuntUuid = treasureHuntUuid

        requestTreasureChestsForTreasureHunt()
        loadTreasureHunt()
        loadInitialClues()
    }

    fun reload(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        requestTreasureChestsForTreasureHunt()
        loadTreasureHunt()
        loadInitialClues()
    }

    override fun unsubscribe() {
        treasureHuntConnection.unsubscribe()
        treasureChestConnection.unsubscribe()

        createHuntView = null
    }

    override fun dispose() {
        treasureHuntConnection.update(TreasureHunt(treasureHuntUuid, treasureHuntTitle))

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureHunt() {
        val treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntUuid)

        treasureHuntTitle = treasureHunt.title

        createHuntView?.setTitle(treasureHuntTitle)
    }

    private fun loadInitialClues() {

    }

    private fun requestTreasureChestsForTreasureHunt() {
        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntUuid, { createHuntView?.onTreasureChestsLoaded(it) })
    }

    private fun createInitialTreasureChest() {
        val initialTreasureChest = TreasureChest(randomUuid(), treasureHuntUuid, "Initial Treasure Chest", true)

        treasureChestConnection.insert(initialTreasureChest)

        initialTreasureChestUuid = initialTreasureChest.uuid
    }

    fun onTitleChanged(newTitle: String) {
        treasureHuntTitle = newTitle
    }
}