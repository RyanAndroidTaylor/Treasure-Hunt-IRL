package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.getLong
import com.dtprogramming.treasurehuntirl.util.getString

/**
 * Created by ryantaylor on 6/21/16.
 */
data class Clue(val id: Long, val uuid: String, val treasureHuntId: String, val text: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, treasureChestId: String, text: String) : this(-1L, uuid, treasureChestId, text)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID), cursor.getString(TABLE.TREASURE_CHEST), cursor.getString(TABLE.TEXT))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TREASURE_CHEST, treasureHuntId)
        contentValues.put(TABLE.TEXT, text)

        return contentValues;
    }

    class Table {
        val NAME: String

        val TREASURE_CHEST: String
        val TEXT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("ClueTable")
            TREASURE_CHEST = quickTable.buildTextColumn("TreasureChest").build()
            TEXT = quickTable.buildTextColumn("Text").build()
            CREATE = quickTable.retrieveCreateString()
        }
    }
}