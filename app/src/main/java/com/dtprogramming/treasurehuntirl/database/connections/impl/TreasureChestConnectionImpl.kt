package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.util.INITIAL_TREASURE_CHEST
import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/11/16.
 */
class TreasureChestConnectionImpl : TreasureChestConnection {

    override val connections = ArrayList<Subscription>()

    override val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(treasureChest: TreasureChest) {
        database.insert(TreasureChest.TABLE.NAME, treasureChest.getContentValues())
    }

    override fun update(treasureChest: TreasureChest) {
        database.update(TreasureChest.TABLE.NAME, treasureChest.getContentValues(), TableColumns.WHERE_UUID_EQUALS, treasureChest.uuid)
    }

    override fun delete(treasureChest: TreasureChest) {
        database.delete(TreasureChest.TABLE.NAME, TableColumns.WHERE_UUID_EQUALS, treasureChest.uuid)
    }

    override fun delete(treasureChestId: String) {
        database.delete(TreasureChest.TABLE.NAME, TableColumns.WHERE_UUID_EQUALS, treasureChestId)
    }

    override fun getNextTreasureChestOrder(treasureHuntUuid: String): Int {
        var treasureChestCount = 0

        val cursor = database.query("SELECT COUNT(*) FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=? AND ${TreasureChest.TABLE.ORDER}!=?", treasureHuntUuid, "-1")

        if (cursor != null && cursor.moveToFirst()) {
            treasureChestCount = cursor.getInt(0)

            cursor.close()
        }

        return treasureChestCount
    }

    override fun getCurrentTreasureChest(treasureHuntUuid: String): TreasureChest? {
        var count = 0

        val cursor = database.query("SELECT COUNT(*) FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${CollectedTreasureChest.TABLE.PLAYING_TREASURE_HUNT}=?", treasureHuntUuid)

        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0) - 1 //Don't count the initial treasure chest

            cursor.close()
        }

        val treasureChestCursor = database.query("SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=? AND ${TreasureChest.TABLE.ORDER}=?", treasureHuntUuid, count.toString())

        if (treasureChestCursor != null && treasureChestCursor.moveToFirst()) {
            val treasureChest = TreasureChest(treasureChestCursor)

            treasureChestCursor.close()

            return treasureChest
        }

        return null
    }

    override fun getTreasureChest(treasureChestUuid: String): TreasureChest {
        val cursor = database.query("SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", treasureChestUuid)

        cursor.moveToFirst()

        val treasureChest = TreasureChest(cursor)

        cursor.close()

        return treasureChest
    }

    override fun getInitialTreasureChest(treasureHuntUuid: String): TreasureChest {
        val cursor = database.query("SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=? AND ${TreasureChest.TABLE.ORDER}=?", treasureHuntUuid, INITIAL_TREASURE_CHEST)

        cursor.moveToFirst()

        val treasureChest = TreasureChest(cursor)

        cursor.close()

        return treasureChest
    }

    override fun getTreasureChestsForTreasureHunt(treasureHuntUuid: String): List<TreasureChest> {
        val treasureChests = ArrayList<TreasureChest>()

        val cursor = database.query("SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=? AND ${TreasureChest.TABLE.ORDER}!=? ORDER BY ${TreasureChest.TABLE.ORDER} ASC", treasureHuntUuid, INITIAL_TREASURE_CHEST)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                treasureChests.add(TreasureChest(cursor))
            }

            cursor.close()
        }

        return treasureChests
    }

    override fun getTreasureChestsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<TreasureChest>) -> Unit) {
        val connection = database.createQuery(TreasureChest.TABLE.NAME, "SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=? AND ${TreasureChest.TABLE.ORDER}!=? ORDER BY ${TreasureChest.TABLE.ORDER} ASC", treasureHuntUuid, INITIAL_TREASURE_CHEST)
                .mapToList { TreasureChest(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe {
                    val treasureChests = ArrayList<TreasureChest>(it.size)

                    for (treasureChest in it)
                        treasureChests.add(treasureChest)

                    onComplete(treasureChests)
                }

        connections.add(connection)
    }

    override fun getTreasureChestCountForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (count: Int) -> Unit) {
        database.createQuery(TreasureChest.TABLE.NAME, "SELECT COUNT(*) FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntUuid)
                .mapToOne { it.getInt(0) }
                .first()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onComplete(it)
                }
    }

    override fun getTreasureChestCountForTreasureHunt(treasureHuntUuid: String): Int {
        val cursor = database.query("SELECT COUNT(*) FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntUuid)

        cursor.moveToFirst()

        val count = cursor.getInt(0)

        cursor.close()

        return count
    }

    override fun getCollectedChestCountForPlayingTreasureHunt(playingTreasureHuntUuid: String): Int {
        val cursor = database.query("SELECT COUNT(*) FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${CollectedTreasureChest.TABLE.PLAYING_TREASURE_HUNT}=?", playingTreasureHuntUuid)

        cursor.moveToFirst()

        val count = cursor.getInt(0)

        cursor.close()

        return count
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}