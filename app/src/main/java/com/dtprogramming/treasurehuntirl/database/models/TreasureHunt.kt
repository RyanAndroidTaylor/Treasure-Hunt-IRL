package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 6/21/16.
 */
data class TreasureHunt(val uuid: String, val clues: List<String>) {

    companion object {
        val TABLE = Table()
    }

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid);

        return contentValues
    }

    class Table : TableColumns {
        val NAME: String

        val TITLE: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.open("TreasureHuntTable")
            TITLE = quickTable.buildTextColumn("Title").build()
            CREATE = quickTable.retrieveCreateString()
        }
    }
}