package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.CollectedClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
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

    override fun subscribeToCollectedCluesForParentAsync(parentId: String, onComplete: (List<Clue>) -> Unit) {
        val connection = database.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME} LEFT JOIN ${CollectedClue.TABLE.NAME} ON ${Clue.TABLE.NAME}.${TableColumns.UUID} = ${CollectedClue.TABLE.NAME}.${TableColumns.UUID} WHERE ${CollectedClue.TABLE.NAME}.${CollectedClue.TABLE.PARENT}=?", parentId)
                .mapToList { Clue(it) }
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