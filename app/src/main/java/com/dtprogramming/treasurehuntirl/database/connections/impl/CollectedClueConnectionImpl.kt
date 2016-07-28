package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.CollectedClueConnection
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/21/16.
 */
class CollectedClueConnectionImpl : CollectedClueConnection {

    override val database = THApp.briteDatabase

    override val connections = ArrayList<Subscription>()

    override fun insert(collectedTextClue: CollectedTextClue) {
        database.insert(CollectedTextClue.TABLE.NAME, collectedTextClue.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getCollectedClue(collectedClueUuid: String): CollectedTextClue {
        val cursor = database.query("SELECT * FROM ${CollectedTextClue.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", collectedClueUuid)

        cursor.moveToFirst()

        val collectedClue = CollectedTextClue(cursor)

        cursor.close()

        return collectedClue
    }

    override fun subscribeToCollectedCluesForTreasureHuntAsync(parentUuid: String, onComplete: (List<CollectedTextClue>) -> Unit) {
        val connection = database.createQuery(CollectedTextClue.TABLE.NAME, "SELECT * FROM ${CollectedTextClue.TABLE.NAME} WHERE ${CollectedTextClue.TABLE.PARENT}=?", parentUuid)
                .mapToList { CollectedTextClue(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onComplete(it)
                }

        connections.add(connection)
    }

    override fun unsubscribe() {
        for (connection in connections)
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
    }
}