package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.*

/**
 * Created by ryantaylor on 7/11/16.
 */
data class TreasureChest(val id: Long, val uuid: String, val treasureHuntUuid: String, val title: String, val order: Int, val state: Int) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, treasureHuntUuid: String, title: String, order: Int, state: Int): this(-1L, uuid, treasureHuntUuid, title, order, state)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID), cursor.getString(TreasureChest.TABLE.TREASURE_HUNT), cursor.getString(TABLE.TITLE), cursor.getInt(TABLE.ORDER), cursor.getInt(TABLE.STATE))

    fun collectTreasureChest(): CollectedTreasureChest {
        val newState = if (state == BURIED)
            CLOSED
        else
            LOCKED

        return CollectedTreasureChest(uuid, title, treasureHuntUuid, newState)
    }

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TABLE.TITLE, title)
        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TREASURE_HUNT, treasureHuntUuid)
        contentValues.put(TABLE.ORDER, order)
        contentValues.put(TABLE.STATE, state)

        return contentValues
    }

    class Table {
        val NAME: String

        val TITLE: String
        val TREASURE_HUNT: String
        val ORDER: String
        val STATE: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("TreasureChestTable")

            TITLE = quickTable.buildTextColumn("Title").build()
            TREASURE_HUNT = quickTable.buildTextColumn("TreasureHunt").build()
            ORDER = quickTable.buildIntColumn("TreasureChestOrder").build()
            STATE = quickTable.buildIntColumn("State").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}