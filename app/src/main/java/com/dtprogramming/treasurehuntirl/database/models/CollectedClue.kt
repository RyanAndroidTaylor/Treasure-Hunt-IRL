package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 7/20/16.
 */
data class CollectedClue(val id: Long, val uuid: String, val parentUuid: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, parentUuid: String): this(-1L, uuid, parentUuid)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.PARENT, parentUuid)

        return contentValues
    }

    class Table {
        val NAME: String

        val PARENT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.openWithUuidForeignKeyRestraint("CollectedClueTable", Clue.TABLE.NAME)
            PARENT = quickTable.buildTextColumn("Parent").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}