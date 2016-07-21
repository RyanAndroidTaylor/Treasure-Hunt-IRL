package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue

/**
 * Created by ryantaylor on 7/21/16.
 */
interface CollectedClueConnection : Connection {

    fun insert(collectedClue: CollectedClue)

    fun subscribeToCollectedCluesForParentAsync(parentId: String, onComplete: (List<Clue>) -> Unit)
}