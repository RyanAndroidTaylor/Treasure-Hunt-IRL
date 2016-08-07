package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.InventoryItem

/**
 * Created by ryantaylor on 7/19/16.
 */
interface PlayTreasureHuntView {

    fun hideTreasureChestAction()
    fun displayBuriedTreasureChestAction()
    fun displayLockedTreasureChestAction()
    fun updateInventoryList(items: List<InventoryItem>)

    fun switchToDigMode()
    fun viewCollectedTreasureChest(treasureChestUuid: String)
}