package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.InventoryItem

/**
 * Created by ryantaylor on 7/26/16.
 */
interface ViewCollectedTreasureChestView {

    fun displayLockedTreasureChest()
    fun displayClosedTreasureChest()
    fun displayOpenedTreasureChest()
    fun displayCollectedItems(collectedItems: List<InventoryItem>)
    fun displayIncorrectPassPhraseGuess()

    fun close()
}