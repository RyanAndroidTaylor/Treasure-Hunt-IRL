package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.getString
import com.dtprogramming.treasurehuntirl.util.getStringOrNull

/**
 * Created by ryantaylor on 6/21/16.
 */
data class TreasureHunt(val id: Long, val uuid: String, val title: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, title: String): this(-1L, uuid, title)

    constructor(cursor: Cursor): this(cursor.getString(TableColumns.UUID), cursor.getString(TreasureHunt.TABLE.TITLE))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid);
        contentValues.put(TABLE.TITLE, title)

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