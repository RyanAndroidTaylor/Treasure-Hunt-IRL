package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {

    fun setTitle(title: String)
    fun onTreasureChestsLoaded(treasureChests: List<TreasureChest>)

    fun displayClues(initialClues: List<Clue>)
}