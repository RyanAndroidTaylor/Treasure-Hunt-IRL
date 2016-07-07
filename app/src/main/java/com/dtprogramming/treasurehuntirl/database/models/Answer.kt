package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 7/7/16.
 */
data class Answer(val id: Long, val uuid: String, val text: String, val waypointId: String) {

    companion object {
        val TABLE = Table()
    }

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TEXT, text)
        contentValues.put(TABLE.WAYPOINT, waypointId)

        return contentValues
    }

    class Table {
        val NAME: String

        val TEXT: String
        val WAYPOINT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("AnswerTable")

            TEXT = quickTable.buildTextColumn("Text").build()
            WAYPOINT = quickTable.buildTextColumn("WaypointId").foreignKey(Clue.TABLE.NAME, TableColumns.UUID).build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}