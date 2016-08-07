package com.dtprogramming.treasurehuntirl.ui.views

/**
 * Created by ryantaylor on 7/22/16.
 */
interface DigModeView {

    fun displayDiggingAnimation(duration: Int)
    fun hideDiggingAnimation()
    fun updateDiggingProgress(progress: Int)
    fun displayCollectedTreasureChest(treasureChestUuid: String?)
}