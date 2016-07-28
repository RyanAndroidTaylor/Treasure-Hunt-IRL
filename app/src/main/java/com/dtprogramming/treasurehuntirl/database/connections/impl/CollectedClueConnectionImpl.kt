package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.CollectedClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
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

    override fun insert(collectedClue: CollectedClue) {
        database.insert(CollectedClue.TABLE.NAME, collectedClue.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getCollectedClue(collectedClueUuid: String): CollectedClue {
        val cursor = database.query("SELECT * FROM ${CollectedClue.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", collectedClueUuid)

        cursor.moveToFirst()

        val collectedClue = CollectedClue(cursor)

        cursor.close()

        return collectedClue
    }

    override fun subscribeToCollectedCluesForTreasureHuntAsync(parentUuid: String, onComplete: (List<CollectedClue>) -> Unit) {
        val connection = database.createQuery(CollectedClue.TABLE.NAME, "SELECT * FROM ${CollectedClue.TABLE.NAME} WHERE ${CollectedClue.TABLE.PARENT}=?", parentUuid)
                .mapToList { CollectedClue(it) }
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