package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Clue

/**
 * Created by ryantaylor on 7/5/16.
 */
interface ClueConnection : Connection {

    fun insert(clue: Clue)
    fun update(clue: Clue)

    fun getClueForTreasureChest(treasureChestId: String): Clue?

    fun subscribeToCollectedCluesForPlayingTreasureHuntAsync(playingTreasureHuntId: String, onComplete: (List<Clue>) -> Unit)
}