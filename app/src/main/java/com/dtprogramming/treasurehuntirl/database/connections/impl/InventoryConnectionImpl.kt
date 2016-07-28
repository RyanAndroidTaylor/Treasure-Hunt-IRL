package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.InventoryConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
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

    override fun collectItemsForTreasureChestAsync(treasureChestUuid: String, onComplete: () -> Unit) {
        Observable.just(treasureChestUuid)
        .single {
            collectCluesForTreasureChest(treasureChestUuid)

            true
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            onComplete()
        }
    }

    private fun collectCluesForTreasureChest(treasureChestUuid: String) {
        val cursor = database.query("SELECT * FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.PARENT}=?", treasureChestUuid)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val collectedClue = CollectedClue(cursor.getString(TableColumns.UUID), cursor.getString(CollectedClue.TABLE.PARENT), cursor.getString(CollectedClue.TABLE.TEXT))

                database.insert(CollectedClue.TABLE.NAME, collectedClue.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
            }

            cursor.close()
        }
    }

    override fun getCollectedItemsForTreasureChestAsync(treasureChestUuid: String, onComplete: (List<InventoryItem>) -> Unit) {
        Observable.just(treasureChestUuid)
        .map{
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

    private fun getCollectedClues(treasureChestUuid: String): List<CollectedClue> {
        val cursor = database.query("SELECT * FROM ${CollectedClue.TABLE.NAME} WHERE ${CollectedClue.TABLE.PARENT}=?", treasureChestUuid)

        val clues = ArrayList<CollectedClue>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                clues.add(CollectedClue(cursor))
            }

            cursor.close()
        }

        return clues
    }
}