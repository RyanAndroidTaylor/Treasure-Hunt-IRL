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

    fun getNextTreasureChestPosition(treasureHuntUuid: String): Int
    fun getCurrentTreasureChest(treasureHuntUuid: String): TreasureChest?
    fun getCurrentTreasureChestState(treasureHuntUuid: String): Int

    fun getTreasureChest(treasureChestUuid: String): TreasureChest
    fun getInitialTreasureChest(treasureHuntUuid: String): TreasureChest
    fun getTreasureChestsForTreasureHunt(treasureHuntUuid: String): List<TreasureChest>
    fun getTreasureChestsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<TreasureChest>) -> Unit)

    fun getTreasureChestCountForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (count: Int) -> Unit)
    fun getTreasureChestCountForTreasureHunt(treasureHuntUuid: String): Int

    fun getCollectedChestCountForPlayingTreasureHunt(playingTreasureHuntUuid: String): Int
}