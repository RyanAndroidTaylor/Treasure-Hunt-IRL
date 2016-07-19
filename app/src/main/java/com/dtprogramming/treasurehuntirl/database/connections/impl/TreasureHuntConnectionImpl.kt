package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.util.getString
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
class TreasureHuntConnectionImpl : TreasureHuntConnection {

    override val connections = ArrayList<Subscription>()

    val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(treasureHunt: TreasureHunt) {
        database.insert(TreasureHunt.TABLE.NAME, treasureHunt.getContentValues())
    }

    override fun update(treasureHunt: TreasureHunt) {
        database.update(TreasureHunt.TABLE.NAME, treasureHunt.getContentValues(), TableColumns.WHERE_UUID_EQUALS, treasureHunt.uuid)
    }

    override fun getTreasureHunt(treasureHuntId: String): TreasureHunt {
        val treasureHunt: TreasureHunt

        val cursor = THApp.briteDatabase.query("SELECT * FROM ${TreasureHunt.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", treasureHuntId)

        cursor.moveToFirst()

        treasureHunt = TreasureHunt(cursor.getString(TableColumns.UUID), cursor.getString(TreasureHunt.TABLE.TITLE))

        cursor.close()

        return treasureHunt
    }

    override fun getTreasureHuntsAsync(onComplete: (List<TreasureHunt>) -> Unit) {
        THApp.briteDatabase.createQuery(TreasureHunt.TABLE.NAME, "SELECT * FROM ${TreasureHunt.TABLE.NAME}")
                .mapToList { TreasureHunt(it.getString(TableColumns.UUID), it.getString(TreasureHunt.TABLE.TITLE)) }
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe {
                    val treasureHunts = ArrayList<TreasureHunt>()

                    for (treasure in it)
                        treasureHunts.add(treasure)

                    onComplete(treasureHunts)
                }
    }

    override fun subscribeToTreasureHunts(onChange: (List<TreasureHunt>) -> Unit) {
        val connection = THApp.briteDatabase.createQuery(TreasureHunt.TABLE.NAME, "SELECT * FROM ${TreasureHunt.TABLE.NAME}")
                .mapToList { TreasureHunt(it.getString(TableColumns.UUID), it.getString(TreasureHunt.TABLE.TITLE)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val treasureHunts = ArrayList<TreasureHunt>()

                    for (treasure in it)
                        treasureHunts.add(treasure)

                    onChange(treasureHunts)
                }

        connections.add(connection)
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}