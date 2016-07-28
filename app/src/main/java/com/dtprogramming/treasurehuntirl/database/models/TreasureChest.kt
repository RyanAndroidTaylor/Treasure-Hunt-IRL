package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.*

/**
 * Created by ryantaylor on 7/11/16.
 */
data class TreasureChest(val id: Long, val uuid: String, val treasureHuntUuid: String, val title: String, val initialChest: Boolean) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, treasureHuntUuid: String, title: String, initialChest: Boolean): this(-1L, uuid, treasureHuntUuid, title, initialChest)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID), cursor.getString(TreasureChest.TABLE.TREASURE_HUNT), cursor.getString(TABLE.TITLE), cursor.getBoolean(TABLE.INITIAL_CHEST))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TABLE.TITLE, title)
        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TREASURE_HUNT, treasureHuntUuid)
        contentValues.put(TABLE.INITIAL_CHEST, if (initialChest) 1 else 0)

        return contentValues
    }

    class Table {
        val NAME: String

        val TITLE: String
        val TREASURE_HUNT: String
        val INITIAL_CHEST: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("TreasureChestTable")

            TITLE = quickTable.buildTextColumn("Title").build()
            TREASURE_HUNT = quickTable.buildTextColumn("TreasureHunt").build()
            INITIAL_CHEST = quickTable.buildIntColumn("InitialChest").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}