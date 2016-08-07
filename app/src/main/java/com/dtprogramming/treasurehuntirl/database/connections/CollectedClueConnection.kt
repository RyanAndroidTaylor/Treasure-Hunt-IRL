package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue

/**
 * Created by ryantaylor on 7/21/16.
 */
interface CollectedClueConnection : Connection {

    fun insert(collectedTextClue: CollectedTextClue)

    fun getCollectedClue(collectedClueUuid: String): CollectedTextClue

    fun subscribeToCollectedCluesForTreasureHuntAsync(parentUuid: String, onComplete: (List<CollectedTextClue>) -> Unit)
}