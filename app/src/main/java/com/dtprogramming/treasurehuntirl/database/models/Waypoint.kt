package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.getDouble
import com.dtprogramming.treasurehuntirl.util.getLong
import com.dtprogramming.treasurehuntirl.util.getString

/**
 * Created by ryantaylor on 6/23/16.
 */
data class Waypoint(val id: Long, val uuid: String, val parentUuid: String, val lat: Double, val long: Double) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, parentUuid: String, lat: Double, long: Double): this(-1L, uuid, parentUuid, lat, long)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID), cursor.getString(Waypoint.TABLE.PARENT), cursor.getDouble(Waypoint.TABLE.LAT), cursor.getDouble(Waypoint.TABLE.LNG))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.PARENT, parentUuid)
        contentValues.put(TABLE.LAT, lat)
        contentValues.put(TABLE.LNG, long)

        return contentValues;
    }

    fun biggerLat(waypoint: Waypoint): Boolean {
        if (lat > waypoint.lat) {
            return true
        } else if (lat == waypoint.lat) {
            return long >= waypoint.long
        }

        return false
    }

    fun biggerLng(waypoint: Waypoint): Boolean {
        if (long > waypoint.long)
            return true
        else if (long == waypoint.long)
            return lat >= waypoint.lat

        return false
    }

    fun distance(waypoint: Waypoint): Double {
        return Math.hypot(lat - waypoint.lat, long - waypoint.long)
    }

    class Table {
        val NAME: String

        val PARENT: String
        val LAT: String
        val LNG: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("Waypoint")
            PARENT = quickTable.buildTextColumn("Parent").unique().build()
            LAT = quickTable.buildTextColumn("Lat").build()
            LNG = quickTable.buildTextColumn("Long").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}