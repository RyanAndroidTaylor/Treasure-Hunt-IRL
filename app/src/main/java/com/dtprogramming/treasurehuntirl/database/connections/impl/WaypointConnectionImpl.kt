package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.getDouble
import com.dtprogramming.treasurehuntirl.database.getString
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

    private val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(waypoint: Waypoint) {
        database.insert(Waypoint.TABLE.NAME, waypoint.getContentValues())
    }

    override fun update(waypoint: Waypoint) {
        database.update(Waypoint.TABLE.NAME, waypoint.getContentValues(), TableColumns.WHERE_UUID_EQUALS, waypoint.uuid)
    }

    override fun getTreasureHuntWaypointsAsync(treasureHuntId: String, onComplete: (List<Waypoint>) -> Unit) {
        database.createQuery(Waypoint.TABLE.NAME, "SELECT * FROM ${Waypoint.TABLE.NAME} WHERE ${Waypoint.TABLE.TREASURE_HUNT}=?", treasureHuntId)
                .mapToList { Waypoint(it.getString(TableColumns.UUID), it.getString(Waypoint.TABLE.TITLE), it.getString(Waypoint.TABLE.TREASURE_HUNT), it.getDouble(Waypoint.TABLE.LAT), it.getDouble(Waypoint.TABLE.LNG)) }
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe {
                    val waypoints = ArrayList<Waypoint>()

                    for (waypoint in it)
                        waypoints.add(waypoint)

                    onComplete(waypoints)
                }
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}