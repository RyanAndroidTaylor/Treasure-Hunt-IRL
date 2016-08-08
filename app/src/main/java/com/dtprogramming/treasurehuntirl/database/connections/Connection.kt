package com.dtprogramming.treasurehuntirl.database.connections

import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
interface Connection {

    val database: BriteDatabase

    val subscriptions: ArrayList<Subscription>

    fun unsubscribe() {
        for (subscription in subscriptions)
            subscription.unsubscribe()

        subscriptions.clear()
    }
}