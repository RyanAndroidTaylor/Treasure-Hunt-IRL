package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem

/**
 * Created by ryantaylor on 7/19/16.
 */
interface PlayTreasureHuntView {

    fun updateInventoryList(clues: List<InventoryItem>)
}