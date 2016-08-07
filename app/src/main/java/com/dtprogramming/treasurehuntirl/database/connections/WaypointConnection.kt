package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Waypoint

/**
 * Created by ryantaylor on 7/5/16.
 */
interface WaypointConnection : Connection {

    fun insert(waypoint: Waypoint)
    fun update(waypoint: Waypoint)

    fun getWaypointForParent(parentUuid: String): Waypoint?
    fun getWaypointsForTreasureHunt(treasureHuntUuid: String): List<Waypoint>
    fun getWaypointsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<Waypoint>) -> Unit)
}