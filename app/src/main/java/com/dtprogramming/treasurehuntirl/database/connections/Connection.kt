package com.dtprogramming.treasurehuntirl.database.connections

import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription

/**
 * Created by ryantaylor on 7/5/16.
 */
interface Connection {

    val database: BriteDatabase

    val connections: List<Subscription>

    fun unsubscribe()
}