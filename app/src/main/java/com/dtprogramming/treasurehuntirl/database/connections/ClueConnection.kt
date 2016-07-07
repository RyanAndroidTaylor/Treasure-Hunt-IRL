package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Clue

/**
 * Created by ryantaylor on 7/5/16.
 */
interface ClueConnection : Connection {

    fun getTreasureHuntCluesAsync(treasureHuntId: String, onComplete: (List<Clue>) -> Unit)
}