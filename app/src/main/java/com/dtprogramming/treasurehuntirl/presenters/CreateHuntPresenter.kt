package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
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

    private var createHuntView: CreateHuntView? = null

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun create(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHuntUuid = randomUuid()

        treasureHuntConnection.insert(TreasureHunt(treasureHuntUuid, treasureHuntTitle))

        createHuntView.setTitle(treasureHuntTitle)
    }

    fun load(createHuntView: CreateHuntView, treasureHuntUuid: String) {
        this.createHuntView = createHuntView
        this.treasureHuntUuid = treasureHuntUuid

        requestTreasureChestsForTreasureHunt()
        loadTreasureHunt()
        loadClue()
    }

    fun reload(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        requestTreasureChestsForTreasureHunt()
        loadTreasureHunt()
        loadClue()
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

    private fun loadClue() {
        val clue = clueConnection.getClueForParent(treasureHuntUuid)

        clue?.let { createHuntView?.displayClue(clue.text) }
    }

    private fun requestTreasureChestsForTreasureHunt() {
        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntUuid, { createHuntView?.onTreasureChestsLoaded(it) })
    }

    fun onTitleChanged(newTitle: String) {
        treasureHuntTitle = newTitle
    }
}