package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.CollectedTreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.ui.views.ViewCollectedTreasureChestView

/**
 * Created by ryantaylor on 7/26/16.
 */
class ViewCollectedTreasureChestPresenter(val collectedTreasureChestConnection: CollectedTreasureChestConnection) : Presenter {

    companion object {
        val TAG: String = ViewCollectedTreasureChestPresenter::class.java.simpleName
    }

    private var viewCollectedTreasureChestView: ViewCollectedTreasureChestView? = null

    lateinit var collectedTreasureChestUuid: String
        private set

    private lateinit var collectedTreasureChest: CollectedTreasureChest

    fun load(viewCollectedTreasureChestView: ViewCollectedTreasureChestView, collectedTreasureChestUuid: String) {
        this.viewCollectedTreasureChestView = viewCollectedTreasureChestView
        this.collectedTreasureChestUuid = collectedTreasureChestUuid

        collectedTreasureChest = collectedTreasureChestConnection.getCollectedTreasureChest(collectedTreasureChestUuid)

        if (!isTreasureChestClosed()) {
            viewCollectedTreasureChestView.displayOpenedTreasureChest()
            subscribeToCollectedItems()
        }
    }

    fun reload(viewCollectedTreasureChestView: ViewCollectedTreasureChestView) {
        this.viewCollectedTreasureChestView = viewCollectedTreasureChestView

        if (!isTreasureChestClosed()) {
            viewCollectedTreasureChestView.displayOpenedTreasureChest()
            subscribeToCollectedItems()
        }
    }

    override fun unsubscribe() {
        viewCollectedTreasureChestView = null

        collectedTreasureChestConnection.unsubscribe()
    }
    override fun dispose() {

        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    fun openTreasureChest() {
        if (isTreasureChestClosed()) {
            viewCollectedTreasureChestView?.displayOpenedTreasureChest()

            collectedTreasureChest = collectedTreasureChestConnection.openCollectedTreasureChest(collectedTreasureChest)

            collectItems()
            subscribeToCollectedItems()
        }
    }

    private fun collectItems() {

    }

    private fun subscribeToCollectedItems() {

    }

    private fun isTreasureChestClosed(): Boolean {
        return collectedTreasureChest.state == CollectedTreasureChest.CLOSED
    }
}