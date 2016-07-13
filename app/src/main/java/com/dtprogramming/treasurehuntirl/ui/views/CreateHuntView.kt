package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.TreasureChest

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {

    fun onTreasureChestsLoaded(treasureChests: List<TreasureChest>)
}