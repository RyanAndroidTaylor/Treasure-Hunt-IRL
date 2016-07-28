package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TextClue

/**
 * Created by ryantaylor on 7/5/16.
 */
interface ClueConnection : Connection {

    fun insert(textClue: TextClue)
    fun update(textClue: TextClue)

    fun getTextClue(textClueUuid: String): TextClue
    fun getTextClueForParent(parentUuid: String): TextClue?

    fun getCluesForParentAsync(parentUuid: String, onComplete: (List<Clue>) -> Unit)
}