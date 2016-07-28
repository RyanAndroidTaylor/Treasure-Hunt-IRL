package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {

    fun setTitle(title: String)
    fun onTreasureChestsLoaded(treasureChests: List<TreasureChest>)

    fun displayClue(initialClues: List<InventoryItem>)
}