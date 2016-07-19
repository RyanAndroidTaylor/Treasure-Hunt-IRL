package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.Waypoint

/**
 * Created by ryantaylor on 7/16/16.
 */
interface ViewTreasureHuntView {

    fun displayArea(lat: Double, lng: Double, radius: Double, zoom: Float)
}