package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem

/**
 * Created by ryantaylor on 7/26/16.
 */
interface CollectedTreasureChestConnection : Connection {

    fun insert(collectedTreasureChest: CollectedTreasureChest)
    fun update(collectedTreasureChest: CollectedTreasureChest)

    fun openCollectedTreasureChest(collectedTreasureChest: CollectedTreasureChest, itemsCollected: (List<InventoryItem>) -> Unit): CollectedTreasureChest

    fun treasureChestIsCollected(treasureChestUuid: String): Boolean
    fun getCollectedTreasureChest(collectedTreasureChestUuid: String): CollectedTreasureChest
}