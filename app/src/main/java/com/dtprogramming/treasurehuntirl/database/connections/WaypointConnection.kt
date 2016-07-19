package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.Waypoint

/**
 * Created by ryantaylor on 7/5/16.
 */
interface WaypointConnection : Connection {

    fun insert(waypoint: Waypoint)
    fun update(waypoint: Waypoint)

    fun getWaypointForTreasureChest(treasureChestId: String): Waypoint?
    fun getWaypointsForTreasureChests(treasureChests: List<TreasureChest>): List<Waypoint>
    fun getWaypointsForTreasureChestsAsync(treasureChests: List<TreasureChest>, onComplete: (List<Waypoint>) -> Unit)
}