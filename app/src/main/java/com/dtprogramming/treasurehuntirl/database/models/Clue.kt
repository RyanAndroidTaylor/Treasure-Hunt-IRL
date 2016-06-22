package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 6/21/16.
 */
data class Clue(val id: Long, val uuid: String, val treasureHuntId: String, val text: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, treasureHuntId: String, text: String) : this(-1L, uuid, treasureHuntId, text)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TREASURE_HUNT, treasureHuntId)
        contentValues.put(TABLE.TEXT, text)

        return contentValues;
    }

    class Table : TableColumns {
        val NAME: String

        val TREASURE_HUNT: String
        val TEXT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("ClueTable")
            TREASURE_HUNT = quickTable.buildTextColumn("TreasureHunt").foreignKey(TreasureHunt.TABLE.NAME, TableColumns.UUID).build()
            TEXT = quickTable.buildTextColumn("Text").build()
            CREATE = quickTable.retrieveCreateString()
        }
    }
}