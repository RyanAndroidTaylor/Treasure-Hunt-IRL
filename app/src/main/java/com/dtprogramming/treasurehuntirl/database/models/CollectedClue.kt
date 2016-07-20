package com.dtprogramming.treasurehuntirl.database.models

import android.content.ContentValues
import com.dtprogramming.treasurehuntirl.database.QuickTable
import com.dtprogramming.treasurehuntirl.database.TableColumns

/**
 * Created by ryantaylor on 7/20/16.
 */
data class CollectedClue(val id: Long, val uuid: String, val playingTreasureHuntUuid: String) {

    companion object {
        val TABLE = Table()
    }

    constructor(uuid: String, playingTreasureHuntUuid: String): this(-1L, uuid, playingTreasureHuntUuid)

    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(TableColumns.UUID, uuid)
        contentValues.put(TABLE.PLAYING_TREASURE_HUNT, playingTreasureHuntUuid)

        return contentValues
    }

    class Table {
        val NAME: String

        val PLAYING_TREASURE_HUNT: String

        val CREATE: String

        init {
            val quickTable = QuickTable()

            NAME = quickTable.openWithUuidForeignKeyRestraint("CollectedClue", Clue.TABLE.NAME)
            PLAYING_TREASURE_HUNT = quickTable.buildTextColumn("PlayingTreasureHunt").build()

            CREATE = quickTable.retrieveCreateString()
        }
    }
}