package com.dtprogramming.treasurehuntirl.ui.views

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by ryantaylor on 6/26/16.
 */
interface CreateWaypointView {

    open fun loadMarker(marker: MarkerOptions): Marker
    fun markerMoved(marker: Marker)
}