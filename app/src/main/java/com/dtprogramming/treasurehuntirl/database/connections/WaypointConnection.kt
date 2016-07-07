package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Waypoint

/**
 * Created by ryantaylor on 7/5/16.
 */
interface WaypointConnection : Connection {

    fun getTreasureHuntWaypointsAsync(treasureHuntId: String, onComplete: (List<Waypoint>) -> Unit)
}