package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import android.database.Cursor
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.util.getLong
import com.dtprogramming.treasurehuntirl.util.getString

/**
 * Created by ryantaylor on 7/20/16.
 */
data class PlayingTreasureHunt(val id: Long, val uuid: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String): this(-1L, uuid)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)

        return contentValues
    }

    class Table {
        val NAME: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.openWithUuidForeignKeyRestraint("PlayingTreasureHunt", TreasureHunt.TABLE.NAME)
            CREATE = quickTable.retrieveCreateString()
        }
    }
}