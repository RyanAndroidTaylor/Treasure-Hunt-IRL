package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.util.Log
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

    override fun getWaypointsForTreasureHunt(treasureHuntUuid: String): List<Waypoint> {
        val treasureChestCursor = database.query("SELECT ${TableColumns.UUID} FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntUuid)

        val treasureChestUuids = ArrayList<String>()
        while (treasureChestCursor.moveToNext()) {
            treasureChestUuids.add(treasureChestCursor.getString(TableColumns.UUID))
        }

        treasureChestCursor.close()

        val argumentCount = StringBuilder()
        for (i in 0..treasureChestUuids.size - 1) {
            argumentCount.append("?")

            if (i != treasureChestUuids.size -1)
                argumentCount.append(",")
        }

        val cursor = database.query("SELECT * FROM ${Waypoint.TABLE.NAME} WHERE ${Waypoint.TABLE.PARENT} IN ($argumentCount)", *treasureChestUuids.toTypedArray())

        val waypoints = ArrayList<Waypoint>()
        while (cursor.moveToNext()) {
            waypoints.add(Waypoint(cursor))
        }

        cursor.close()

        return waypoints
    }

    override fun getWaypointsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<Waypoint>) -> Unit) {
        database.createQuery(TreasureChest.TABLE.NAME, "SELECT ${TableColumns.UUID} FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntUuid)
                .mapToList { it.getString(TableColumns.UUID) }
                .map { treasureChestUuids ->
                    val waypoints = ArrayList<Waypoint>()

                    val argumentCount = StringBuilder()
                    for (i in 0..treasureChestUuids.size - 1) {
                        argumentCount.append("?")

                        if (i != treasureChestUuids.size -1)
                            argumentCount.append(",")
                    }

                    val cursor = database.query("SELECT * FROM ${Waypoint.TABLE.NAME} WHERE ${Waypoint.TABLE.PARENT} IN ($argumentCount)", *treasureChestUuids.toTypedArray())

                    while (cursor.moveToNext()) {
                        waypoints.add(Waypoint(cursor))
                    }

                    cursor.close()

                    waypoints
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe {
                    onComplete(it)
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