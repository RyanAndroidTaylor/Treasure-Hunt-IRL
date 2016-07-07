package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.getString
import com.dtprogramming.treasurehuntirl.database.models.Clue
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
class ClueConnectionImpl : ClueConnection {

    override val connections = ArrayList<Subscription>()

    override fun getTreasureHuntCluesAsync(treasureHuntId: String, onComplete: (List<Clue>) -> Unit) {

        val connection = THApp.briteDatabase.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.TREASURE_HUNT}=?", treasureHuntId)
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

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}