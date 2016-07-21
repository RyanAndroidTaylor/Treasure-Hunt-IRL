package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.util.getDouble
import com.dtprogramming.treasurehuntirl.util.getString
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
class WaypointConnectionImpl : WaypointConnection {

    override val connections = ArrayList<Subscription>()

    override val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(waypoint: Waypoint) {
        database.insert(Waypoint.TABLE.NAME, waypoint.getContentValues())
    }

    override fun update(waypoint: Waypoint) {
        database.update(Waypoint.TABLE.NAME, waypoint.getContentValues(), TableColumns.WHERE_UUID_EQUALS, waypoint.uuid)
    }

    override fun getWaypointForParent(parentUuid: String): Waypoint? {
        val cursor = database.query("SELECT * FROM ${Waypoint.TABLE.NAME} WHERE ${Waypoint.TABLE.PARENT}=?", parentUuid)

        var waypoint: Waypoint? = null

        if (cursor.moveToFirst())
            waypoint = Waypoint(cursor)

        cursor.close()

        return waypoint
    }

    override fun getWaypointsForTreasureChests(treasureChests: List<TreasureChest>): List<Waypoint> {
        val waypoints = ArrayList<Waypoint>()

        for ((id, uuid) in treasureChests) {
            val waypoint = getWaypointForParent(uuid)

            waypoint?.let { waypoints.add(it) }
        }

        return waypoints
    }

    override fun getWaypointsForTreasureChestsAsync(treasureChests: List<TreasureChest>, onComplete: (List<Waypoint>) -> Unit) {
        //TODO make this async
        val waypoints = ArrayList<Waypoint>()

        for ((id, uuid) in treasureChests) {
            val waypoint = getWaypointForParent(uuid)

            waypoint?.let { waypoints.add(it) }
        }

        onComplete(waypoints)
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}