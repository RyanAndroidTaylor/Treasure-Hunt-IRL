package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt

/**
 * Created by ryantaylor on 7/5/16.
 */
interface TreasureHuntConnection : Connection {

    fun insert(treasureHunt: TreasureHunt)

    fun update(treasureHunt: TreasureHunt)

    fun getTreasureHunt(treasureHuntId: String): TreasureHunt

    fun getTreasureHuntsAsync(onComplete: (List<TreasureHunt>) -> Unit)
    fun subscribeToTreasureHunts(onChange: (List<TreasureHunt>) -> Unit)
}