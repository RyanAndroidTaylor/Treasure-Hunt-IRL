package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 6/23/16.
 */
data class Waypoint(val id: Long, val uuid: String, val title: String, val treasureHuntId: String, val lat: Double, val long: Double) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, title: String, treasureHuntId: String, lat: Double, long: Double): this(-1L, uuid, title, treasureHuntId, lat, long)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TITLE, title)
        contentValues.put(TABLE.TREASURE_HUNT, treasureHuntId)
        contentValues.put(TABLE.LAT, lat)
        contentValues.put(TABLE.LNG, long)

        return contentValues;
    }

    class Table {
        val NAME: String

        val TITLE: String
        val TREASURE_HUNT: String
        val LAT: String
        val LNG: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("Waypoint")
            TITLE = quickTable.buildTextColumn("Title").build()
            TREASURE_HUNT = quickTable.buildTextColumn("TreasureHunt").foreignKey(TreasureHunt.TABLE.NAME, TableColumns.UUID).build()
            LAT = quickTable.buildTextColumn("Lat").build()
            LNG = quickTable.buildTextColumn("Long").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}