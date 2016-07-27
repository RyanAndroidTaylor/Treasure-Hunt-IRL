package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest

/**
 * Created by ryantaylor on 7/26/16.
 */
interface CollectedTreasureChestConnection : Connection {

    fun insert(collectedTreasureChest: CollectedTreasureChest)
    fun update(collectedTreasureChest: CollectedTreasureChest)

    fun openCollectedTreasureChest(collectedTreasureChest: CollectedTreasureChest): CollectedTreasureChest

    fun getCollectedTreasureChest(collectedTreasureChestUuid: String): CollectedTreasureChest
}