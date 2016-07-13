package com.dtprogramming.treasurehuntirl.ui.views

/**
 * Created by ryantaylor on 6/26/16.
 */
interface CreateWaypointView {

    fun loadMarker(lat: Double, lng: Double)
    fun markerMoved(lat: Double, lng: Double)

    fun close()
}