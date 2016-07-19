package com.dtprogramming.treasurehuntirl.ui.views

/**
 * Created by ryantaylor on 7/16/16.
 */
interface ViewTreasureHuntView {

    fun displayTitle(title: String)
    fun displayArea(lat: Double, lng: Double, radius: Double, zoom: Float)
    fun displayTreasureChestCount(count: Int)
}