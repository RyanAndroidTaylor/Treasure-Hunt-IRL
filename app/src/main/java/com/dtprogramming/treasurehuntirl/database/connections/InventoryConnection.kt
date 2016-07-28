package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.InventoryItem

/**
 * Created by ryantaylor on 7/27/16.
 */
interface InventoryConnection : Connection {

    fun collectItemsForTreasureChestAsync(treasureChestUuid: String, onComplete: () -> Unit)

    fun getCollectedItemsForTreasureChestAsync(treasureChestUuid: String, onComplete: (List<InventoryItem>) -> Unit)
}