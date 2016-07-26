package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.CollectedClue

/**
 * Created by ryantaylor on 7/19/16.
 */
interface PlayTreasureHuntView {

    fun updateInventoryList(clues: List<CollectedClue>)
}