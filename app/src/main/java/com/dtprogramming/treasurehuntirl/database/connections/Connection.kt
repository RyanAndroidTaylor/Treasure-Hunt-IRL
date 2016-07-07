package com.dtprogramming.treasurehuntirl.database.connections

import rx.Subscription

/**
 * Created by ryantaylor on 7/5/16.
 */
interface Connection {

    val connections: List<Subscription>

    fun unsubscribe()
}