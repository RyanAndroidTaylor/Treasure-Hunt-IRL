package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.CollectedTreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.util.getString
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by ryantaylor on 7/26/16.
 */
class CollectedTreasureChestConnectionImpl(override val database: BriteDatabase) : CollectedTreasureChestConnection {

    override val subscriptions = ArrayList<Subscription>()

    override fun insert(collectedTreasureChest: CollectedTreasureChest) {
        database.insert(CollectedTreasureChest.TABLE.NAME, collectedTreasureChest.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun update(collectedTreasureChest: CollectedTreasureChest) {
        database.update(CollectedTreasureChest.TABLE.NAME, collectedTreasureChest.getContentValues(), TableColumns.WHERE_UUID_EQUALS, collectedTreasureChest.uuid)
    }

    override fun openCollectedTreasureChest(collectedTreasureChest: CollectedTreasureChest, itemsCollected: (List<InventoryItem>) -> Unit): CollectedTreasureChest {
        val openedTreasureChest = collectedTreasureChest.open()

        update(openedTreasureChest)

        Observable.just(collectedTreasureChest)
                .map {
                    collectItemsForOpenedTreasureChest(openedTreasureChest.uuid)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    itemsCollected(it)
                }

        return openedTreasureChest
    }

    override fun treasureChestIsCollected(treasureChestUuid: String): Boolean {
        val cursor = database.query("SELECT COUNT(*) FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", treasureChestUuid)

        var isCollected = false

        if (cursor != null && cursor.moveToFirst()) {
            isCollected = cursor.getInt(0) > 0

            cursor.close()
        }

        return isCollected
    }

    override fun getCollectedTreasureChest(collectedTreasureChestUuid: String): CollectedTreasureChest {
        val cursor = database.query("SELECT * FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", collectedTreasureChestUuid)

        cursor.moveToFirst()

        val collectedTreasureChest = CollectedTreasureChest(cursor)

        cursor.close()

        return collectedTreasureChest
    }

    private fun collectItemsForOpenedTreasureChest(treasureChestUuid: String): List<InventoryItem> {
        val cursor = database.query("SELECT * FROM ${TextClue.TABLE.NAME} WHERE ${TextClue.TABLE.PARENT}=?", treasureChestUuid)

        val inventoryItems = ArrayList<InventoryItem>(cursor.count)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val collectedClue = CollectedTextClue(cursor.getString(TableColumns.UUID), cursor.getString(CollectedTextClue.TABLE.PARENT), cursor.getString(CollectedTextClue.TABLE.TEXT))

                database.insert(CollectedTextClue.TABLE.NAME, collectedClue.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)

                inventoryItems.add(collectedClue)
            }

            cursor.close()
        }

        return inventoryItems
    }
}