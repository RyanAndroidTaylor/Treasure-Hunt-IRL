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

    override fun getClueForTreasureChest(treasureChestId: String): Clue? {
        val cursor = database.query("SELECT * FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.TREASURE_CHEST}=?", treasureChestId)

        var clue: Clue? = null

        if (cursor != null && cursor.moveToFirst())
            clue = Clue(cursor)

        cursor.close()

        return clue
    }

    override fun unsubscribe() {
        for (connection in connections) {
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
        }

        connections.clear()
    }
}