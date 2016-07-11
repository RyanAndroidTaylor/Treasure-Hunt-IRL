package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 7/11/16.
 */
data class TreasureChest(val id: Long, val uuid: String, val waypointId: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, waypointId: String): this(-1L, uuid, waypointId)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.WAYPOINT, waypointId)

        return contentValues
    }

    class Table {
        val NAME: String

        val WAYPOINT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("TreasureChestTable")

            WAYPOINT = quickTable.buildTextColumn("Waypoint").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}