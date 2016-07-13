package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 6/23/16.
 */
data class Waypoint(val id: Long, val uuid: String, val treasureChestId: String, val lat: Double, val long: Double) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, treasureChestId: String, lat: Double, long: Double): this(-1L, uuid, treasureChestId, lat, long)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TREASURE_CHEST, treasureChestId)
        contentValues.put(TABLE.LAT, lat)
        contentValues.put(TABLE.LNG, long)

        return contentValues;
    }

    class Table {
        val NAME: String

        val TREASURE_CHEST: String
        val LAT: String
        val LNG: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("Waypoint")
            TREASURE_CHEST = quickTable.buildTextColumn("TreasureChest").build()
            LAT = quickTable.buildTextColumn("Lat").build()
            LNG = quickTable.buildTextColumn("Long").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}