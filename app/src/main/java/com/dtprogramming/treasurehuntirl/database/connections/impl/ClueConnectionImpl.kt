package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
import com.dtprogramming.treasurehuntirl.util.getString
import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
class ClueConnectionImpl : ClueConnection {

    override val connections = ArrayList<Subscription>()

    private val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(clue: Clue) {
        database.insert(Clue.TABLE.NAME, clue.getContentValues())
    }

    override fun update(clue: Clue) {
        database.update(Clue.TABLE.NAME, clue.getContentValues(), TableColumns.WHERE_UUID_EQUALS, clue.uuid)
    }

    override fun getClue(clueId: String): Clue {
        val cursor = database.query("SELECT * FROM ${Clue.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", clueId)

        cursor.moveToFirst()

        val clue = Clue(cursor)

        cursor.close()

        return clue
    }

    override fun getClueForParent(parentId: String): Clue? {
        val cursor = database.query("SELECT * FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.PARENT}=?", parentId)

        var clue: Clue? = null

        if (cursor != null && cursor.moveToFirst())
            clue = Clue(cursor)

        cursor.close()

        return clue
    }

    override fun subscribeToCollectedCluesForParentAsync(parentId: String, onComplete: (List<Clue>) -> Unit) {
        val connection = database.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME} LEFT JOIN ${CollectedClue.TABLE.NAME} ON ${Clue.TABLE.NAME}.${TableColumns.UUID} = ${CollectedClue.TABLE.NAME}.${TableColumns.UUID} WHERE ${CollectedClue.TABLE.PARENT}=?", parentId)
                .mapToList { Clue(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onComplete(it)
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