package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.TreasureChest

/**
 * Created by ryantaylor on 7/11/16.
 */
interface TreasureChestConnection : Connection {

    fun insert(treasureChest: TreasureChest)
    fun update(treasureChest: TreasureChest)
    fun delete(treasureChest: TreasureChest)
    fun delete(treasureChestId: String)

    fun getTreasureChest(treasureChestId: String): TreasureChest
    fun getTreasureChestsForTreasureHuntAsync(treasureHuntId: String, onComplete: (List<TreasureChest>) -> Unit)

    fun getTreasureChestCountForTreasureHunt(treasureHuntId: String, onComplete: (count: Int) -> Unit)
}