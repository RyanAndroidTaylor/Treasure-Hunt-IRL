package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteDatabase
import android.support.design.widget.TabLayout
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.InventoryConnection
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
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

    override val connections = ArrayList<Subscription>()

    override fun getCollectedItemsForTreasureHuntAsync(treasureHuntUuid: String, onComplete: (List<InventoryItem>) -> Unit) {
        Observable.just(treasureHuntUuid)
        .map {
            val inventoryItems = ArrayList<InventoryItem>()

            val openTreasureChestCursor = database.query("SELECT ${TableColumns.UUID} FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${CollectedTreasureChest.TABLE.PLAYING_TREASURE_HUNT}=? AND ${CollectedTreasureChest.TABLE.STATE}=?", treasureHuntUuid, CollectedTreasureChest.OPEN.toString())

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
                    val inventoryItems = ArrayList<InventoryItem>()

                    val clues = getCollectedClues(treasureChestUuid)

                    for (clue in clues)
                        inventoryItems.add(clue)

                    inventoryItems
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onComplete(it)
                }
    }

    override fun unsubscribe() {
        for (connection in connections)
            connection.unsubscribe()
    }

    private fun getCollectedClues(treasureChestUuid: String): List<InventoryItem> {
        val cursor = database.query("SELECT * FROM ${CollectedTextClue.TABLE.NAME} WHERE ${CollectedTextClue.TABLE.PARENT}=?", treasureChestUuid)

        val clues = ArrayList<CollectedTextClue>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                clues.add(CollectedTextClue(cursor))
            }

            cursor.close()
        }

        return clues
    }
}