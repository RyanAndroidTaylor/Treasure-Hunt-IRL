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
data class CollectedClue(val id: Long, val uuid: String, val parentUuid: String, val text: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, parentUuid: String, text: String): this(-1L, uuid, parentUuid, text)

    constructor(cursor: Cursor): this(cursor.getLong(TableColumns.ID), cursor.getString(TableColumns.UUID), cursor.getString(TABLE.PARENT), cursor.getString(TABLE.TEXT))

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.PARENT, parentUuid)
        contentValues.put(TABLE.TEXT, text)

        return contentValues
    }

    class Table {
        val NAME: String

        val PARENT: String
        val TEXT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.openWithUuidForeignKeyRestraint("CollectedClueTable", Clue.TABLE.NAME)
            PARENT = quickTable.buildTextColumn("Parent").build()
            TEXT = quickTable.buildTextColumn("Text").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}