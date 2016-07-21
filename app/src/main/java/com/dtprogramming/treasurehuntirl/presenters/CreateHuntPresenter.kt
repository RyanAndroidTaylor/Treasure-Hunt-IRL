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

    lateinit var treasureHuntId: String
        private set
    var initialClueId: String? = null
        private set

    private var createHuntView: CreateHuntView? = null

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun create(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHuntId = randomUuid()

        treasureHuntConnection.insert(TreasureHunt(treasureHuntId, treasureHuntTitle, null))

        createHuntView.setTitle(treasureHuntTitle)
    }

    fun load(createHuntView: CreateHuntView, treasureHuntId: String) {
        this.createHuntView = createHuntView
        this.treasureHuntId = treasureHuntId

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

    override fun finish() {
        treasureHuntConnection.update(TreasureHunt(treasureHuntId, treasureHuntTitle, initialClueId))

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureHunt() {
        val treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntId)

        treasureHuntTitle = treasureHunt.title
        initialClueId = treasureHunt.initialClueUuid

        createHuntView?.setTitle(treasureHuntTitle)
    }

    private fun loadClue() {
        initialClueId?.let {
            val clue = clueConnection.getClue(it)

            createHuntView?.displayClue(clue.text)
        }
    }

    private fun requestTreasureChestsForTreasureHunt() {
        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntId, { createHuntView?.onTreasureChestsLoaded(it) })
    }

    fun onTitleChanged(newTitle: String) {
        treasureHuntTitle = newTitle
    }
}