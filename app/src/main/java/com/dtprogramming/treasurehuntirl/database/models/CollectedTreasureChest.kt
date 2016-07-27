package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.getInt
import com.dtprogramming.treasurehuntirl.util.getLong
import com.dtprogramming.treasurehuntirl.util.getString

/**
 * Created by ryantaylor on 7/20/16.
 */
data class CollectedTreasureChest(val id: Long, val uuid: String, val title: String, val playingTreasureHuntUuid: String, val state: Int) {

    companion object {
        val TABLE = Table()

        val CLOSED = 0
        val OPEN = 1
    }

    constructor(uuid: String, title: String, playingTreasureHuntUuid: String, state: Int): this(-1L, uuid, title, playingTreasureHuntUuid, state)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID), cursor.getString(TABLE.TITLE), cursor.getString(TABLE.PLAYING_TREASURE_HUNT), cursor.getInt(TABLE.STATE))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TITLE, title)
        contentValues.put(TABLE.PLAYING_TREASURE_HUNT, playingTreasureHuntUuid)
        contentValues.put(TABLE.STATE, state)

        return contentValues
    }

    class Table {
        val NAME: String
        val TITLE: String
        val PLAYING_TREASURE_HUNT: String
        val STATE: String
        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.openWithUuidForeignKeyRestraint("CollectedTreasureChest", TreasureChest.TABLE.NAME)
            TITLE = quickTable.buildTextColumn("Title").build()
            PLAYING_TREASURE_HUNT = quickTable.buildTextColumn("PlayingTreasureHunt").build()
            STATE = quickTable.buildIntColumn("State").build()
            CREATE = quickTable.retrieveCreateString()
        }
    }
}