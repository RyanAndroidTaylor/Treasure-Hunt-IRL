package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.util.getDouble
import com.dtprogramming.treasurehuntirl.util.getString
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.util.BURIED
import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
class WaypointConnectionImpl : WaypointConnection {

    override val subscriptions = ArrayList<Subscription>()

    override val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(waypoint: Waypoint) {
        database.insert(Waypoint.TABLE.NAME, waypoint.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
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
        val treasureChestCursor = database.query("SELECT ${TableColumns.UUID} FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=? AND ${TreasureChest.TABLE.STATE}=?", treasureHuntUuid, BURIED.toString())

        val treasureChestUuids = ArrayList<String>(treasureChestCursor.count)

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

        val waypoints = ArrayList<Waypoint>(cursor.count)
        while (cursor.moveToNext()) {
            waypoints.add(Waypoint(cursor))
        }

        cursor.close()

        return waypoints
    }

    override fun getWaypointsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<Waypoint>) -> Unit) {
        val subscription = database.createQuery(TreasureChest.TABLE.NAME, "SELECT ${TableColumns.UUID} FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntUuid)
                .mapToList { it.getString(TableColumns.UUID) }
                .map { treasureChestUuids ->
                    val argumentCount = StringBuilder()
                    for (i in 0..treasureChestUuids.size - 1) {
                        argumentCount.append("?")

                        if (i != treasureChestUuids.size -1)
                            argumentCount.append(",")
                    }

                    val cursor = database.query("SELECT * FROM ${Waypoint.TABLE.NAME} WHERE ${Waypoint.TABLE.PARENT} IN ($argumentCount)", *treasureChestUuids.toTypedArray())

                    val waypoints = ArrayList<Waypoint>(cursor.count)

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

        subscriptions.add(subscription)
    }

    override fun unsubscribe() {
        for (connection in subscriptions) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        subscriptions.clear()
    }
}