package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 6/21/16.
 */
data class Clue(val id: Long, val uuid: String, val treasureHuntId: String, val answerId: String, val text: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, treasureHuntId: String, answerId: String, text: String) : this(-1L, uuid, treasureHuntId, answerId, text)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.TREASURE_HUNT, treasureHuntId)
        contentValues.put(TABLE.ANSWER, answerId)
        contentValues.put(TABLE.TEXT, text)

        return contentValues;
    }

    class Table {
        val NAME: String

        val TREASURE_HUNT: String
        val ANSWER: String
        val TEXT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("ClueTable")
            TREASURE_HUNT = quickTable.buildTextColumn("TreasureHunt").foreignKey(TreasureHunt.TABLE.NAME, TableColumns.UUID).build()
            ANSWER = quickTable.buildTextColumn("Answer").build()
            TEXT = quickTable.buildTextColumn("Text").build()
            CREATE = quickTable.retrieveCreateString()
        }
    }
}