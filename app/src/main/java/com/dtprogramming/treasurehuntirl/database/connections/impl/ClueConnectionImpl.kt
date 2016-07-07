package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
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

    override fun getTreasureHuntCluesAsync(treasureHuntId: String, onComplete: (List<Clue>) -> Unit) {

        val connection = database.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.TREASURE_HUNT}=?", treasureHuntId)
                .mapToList { Clue(it.getString(TableColumns.UUID), it.getString(Clue.TABLE.TREASURE_HUNT), it.getString(Clue.TABLE.TEXT)) }
                .observeOn(AndroidSchedulers.mainThread())
                .first()
                .subscribe {
                    val clues = ArrayList<Clue>()

                    for (clue in it)
                        clues.add(clue)

                    onComplete(clues)
                }

        connections.add(connection)
    }

    override fun getClueCountForTreasureHunt(treasureHuntId: String): Int {
        val cursor = database.query("SELECT COUNT(*) FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.TREASURE_HUNT}=?", treasureHuntId)

        val clueCount = if  (cursor.moveToFirst())
            cursor.getInt(0)
        else
            0

        cursor.close()

        return clueCount
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}