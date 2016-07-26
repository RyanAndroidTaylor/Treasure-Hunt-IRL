package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
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

    override fun unsubscribe() {
        for (connection in connections)
            connection.unsubscribe()
    }
}