package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.CollectedTreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 7/26/16.
 */
class CollectedTreasureChestConnectionImpl : CollectedTreasureChestConnection {

    override val database = THApp.briteDatabase
    override val connections = ArrayList<Subscription>()

    override fun insert(collectedTreasureChest: CollectedTreasureChest) {
        database.insert(CollectedTreasureChest.TABLE.NAME, collectedTreasureChest.getContentValues())
    }

    override fun update(collectedTreasureChest: CollectedTreasureChest) {
        database.update(CollectedTreasureChest.TABLE.NAME, collectedTreasureChest.getContentValues(), TableColumns.WHERE_UUID_EQUALS, collectedTreasureChest.uuid)
    }

    override fun openCollectedTreasureChest(collectedTreasureChest: CollectedTreasureChest): CollectedTreasureChest {
        val openedTreasureChest = CollectedTreasureChest(collectedTreasureChest.uuid, collectedTreasureChest.title, collectedTreasureChest.playingTreasureHuntUuid, CollectedTreasureChest.OPEN)

        update(openedTreasureChest)

        return openedTreasureChest
    }

    override fun getCollectedTreasureChest(collectedTreasureChestUuid: String): CollectedTreasureChest {
        val cursor = database.query("SELECT * FROM ${CollectedTreasureChest.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", collectedTreasureChestUuid)

        cursor.moveToFirst()

        val collectedTreasureChest = CollectedTreasureChest(cursor)

        cursor.close()

        return collectedTreasureChest
    }

    override fun unsubscribe() {
        for (connection in connections)
            connection.unsubscribe()
    }
}