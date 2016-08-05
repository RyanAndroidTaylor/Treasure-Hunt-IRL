package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.PlayingTreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * Created by ryantaylor on 7/20/16.
 */
class PlayingTreasureHuntConnectionImpl : PlayingTreasureHuntConnection {

    override val database = THApp.briteDatabase

    override val subscriptions = ArrayList<Subscription>()

    override fun insert(playingTreasureHunt: PlayingTreasureHunt) {
        database.insert(PlayingTreasureHunt.TABLE.NAME, playingTreasureHunt.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun getPlayingTreasureHuntsAsync(onComplete: (List<PlayingTreasureHunt>) -> Unit) {
        val subscription = database.createQuery(PlayingTreasureHunt.TABLE.NAME, "SELECT * FROM ${PlayingTreasureHunt.TABLE.NAME}")
                .mapToList { PlayingTreasureHunt(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onComplete(it)
                }

        subscriptions.add(subscription)
    }

    override fun unsubscribe() {
        for (connection in subscriptions)
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
    }
}