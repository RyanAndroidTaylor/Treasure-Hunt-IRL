package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.Clue

/**
 * Created by ryantaylor on 7/19/16.
 */
interface PlayTreasureHuntView {

    fun updateInventoryList(clues: List<Clue>)

    fun displayFoundTreasureChest(foundTreasureChest: String)
}