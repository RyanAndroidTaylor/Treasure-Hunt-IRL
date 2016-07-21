package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.util.getString
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

    override fun getTreasureChest(treasureChestId: String): TreasureChest {
        val cursor = database.query("SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", treasureChestId)

        cursor.moveToFirst()

        val treasureChest = TreasureChest(cursor)

        return treasureChest
    }

    override fun getTreasureChestsForTreasureHunt(treasureHuntId: String): List<TreasureChest> {
        val treasureChests = ArrayList<TreasureChest>()

        val cursor = database.query("SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntId)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                treasureChests.add(TreasureChest(cursor))
            }

            cursor.close()
        }

        return treasureChests
    }

    override fun getTreasureChestsForTreasureHuntAsync(treasureHuntId: String, onComplete: (List<TreasureChest>) -> Unit) {
        val connection = database.createQuery(TreasureChest.TABLE.NAME, "SELECT * FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntId)
                .mapToList { TreasureChest(it.getString(TableColumns.UUID), it.getString(TreasureChest.TABLE.TREASURE_HUNT), it.getString(TreasureChest.TABLE.TITLE)) }
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

    //TODO Turn this async once we are figuring out the count of all different types of treasure chests
    override fun getTreasureChestCountForTreasureHunt(treasureHuntId: String, onComplete: (count: Int) -> Unit) {
        val cursor = database.query("SELECT COUNT(*) FROM ${TreasureChest.TABLE.NAME} WHERE ${TreasureChest.TABLE.TREASURE_HUNT}=?", treasureHuntId)

        cursor.moveToFirst()

        val count = cursor.getInt(0)

        cursor.close()

        onComplete(count)
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}