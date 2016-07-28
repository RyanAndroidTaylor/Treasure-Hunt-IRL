package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.CollectedTreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.InventoryConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.ui.views.ViewCollectedTreasureChestView

/**
 * Created by ryantaylor on 7/26/16.
 */
class ViewCollectedTreasureChestPresenter(val collectedTreasureChestConnection: CollectedTreasureChestConnection, val inventoryConnection: InventoryConnection) : Presenter {

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

        if (isTreasureChestOpen()) {
            viewCollectedTreasureChestView.displayOpenedTreasureChest()
            getAndDisplayCollectedItems()
        }
    }

    fun reload(viewCollectedTreasureChestView: ViewCollectedTreasureChestView) {
        this.viewCollectedTreasureChestView = viewCollectedTreasureChestView

        if (isTreasureChestOpen()) {
            viewCollectedTreasureChestView.displayOpenedTreasureChest()
            getAndDisplayCollectedItems()
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
        if (!isTreasureChestOpen()) {
            viewCollectedTreasureChestView?.displayOpenedTreasureChest()

            collectedTreasureChest = collectedTreasureChestConnection.openCollectedTreasureChest(collectedTreasureChest)

            collectItems()
        }
    }

    private fun collectItems() {
        inventoryConnection.collectItemsForTreasureChestAsync(collectedTreasureChestUuid, { getAndDisplayCollectedItems() })
    }

    private fun getAndDisplayCollectedItems() {
        inventoryConnection.getCollectedItemsForTreasureChestAsync(collectedTreasureChestUuid, { viewCollectedTreasureChestView?.displayCollectedItems(it) })
    }

    private fun isTreasureChestOpen(): Boolean {
        return collectedTreasureChest.state == CollectedTreasureChest.OPEN
    }
}