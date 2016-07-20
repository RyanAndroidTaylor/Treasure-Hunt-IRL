package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 7/20/16.
 */
data class CollectedTreasureChest(val id: Long, val uuid: String, val playingTreasureHuntUuid: String, val state: Int) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, playingTreasureHuntUuid: String, state: Int): this(-1L, uuid, playingTreasureHuntUuid, state)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.PLAYING_TREASURE_HUNT, playingTreasureHuntUuid)
        contentValues.put(TABLE.STATE, state)

        return contentValues
    }

    class Table {
        val NAME: String
        val PLAYING_TREASURE_HUNT: String
        val STATE: String
        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.openWithUuidForeignKeyRestraint("CollectedTreasureChest", TreasureChest.TABLE.NAME)
            PLAYING_TREASURE_HUNT = quickTable.buildTextColumn("PlayingTreasureHunt").build()
            STATE = quickTable.buildIntColumn("State").build()
            CREATE = quickTable.retrieveCreateString()
        }
    }
}