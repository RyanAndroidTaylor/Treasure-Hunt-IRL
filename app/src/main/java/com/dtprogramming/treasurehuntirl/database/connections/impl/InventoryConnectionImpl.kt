package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.InventoryConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.util.LOCKED
import com.dtprogramming.treasurehuntirl.util.OPEN
import com.dtprogramming.treasurehuntirl.util.getString
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by ryantaylor on 7/27/16.
 */
class InventoryConnectionImpl : InventoryConnection {
    override val database = THApp.briteDatabase

    override val subscriptions = ArrayList<Subscription>()

    override fun getCollectedItemsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<InventoryItem>) -> Unit) {
        Observable.just(treasureHuntUuid)
        .map {
            val openTreasureChestCursor = database.query("SELECT ${TableColumns.UUID} FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${CollectedTreasureChest.TABLE.PLAYING_TREASURE_HUNT}=? AND ${CollectedTreasureChest.TABLE.STATE}=?", treasureHuntUuid, OPEN.toString())

            val inventoryItems = ArrayList<InventoryItem>(openTreasureChestCursor.count)

            while (openTreasureChestCursor.moveToNext()) {
                val clues = getCollectedClues(openTreasureChestCursor.getString(TableColumns.UUID))

                for (clue in clues)
                    inventoryItems.add(clue)
            }

            openTreasureChestCursor.close()

            inventoryItems
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { onComplete(it) }
    }

    override fun getCollectedItemsForTreasureChestAsync(treasureChestUuid: String, onComplete: (List<InventoryItem>) -> Unit) {
        Observable.just(treasureChestUuid)
                .map {
                    val clues = getCollectedClues(treasureChestUuid)

                    clues
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onComplete(it)
                }
    }

    override fun unsubscribe() {
        for (connection in subscriptions)
            connection.unsubscribe()
    }

    private fun getCollectedClues(treasureChestUuid: String): List<InventoryItem> {
        val cursor = database.query("SELECT * FROM ${CollectedTextClue.TABLE.NAME} WHERE ${CollectedTextClue.TABLE.PARENT}=?", treasureChestUuid)

        val clues = ArrayList<CollectedTextClue>(cursor.count)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                clues.add(CollectedTextClue(cursor))
            }

            cursor.close()
        }

        return clues
    }

    private fun getCurrentTreasureChest(treasureHuntUuid: String): TreasureChest? {
        var count = 0

        val cursor = database.query("SELECT COUNT(*) FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${CollectedTreasureChest.TABLE.PLAYING_TREASURE_HUNT}=? AND ${CollectedTreasureChest.TABLE.STATE}=?", treasureHuntUuid, OPEN.toString())

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
}