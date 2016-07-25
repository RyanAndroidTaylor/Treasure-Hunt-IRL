package com.dtprogramming.treasurehuntirl.util

import android.util.Log
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by ryantaylor on 7/23/16.
 */
class DiggingTimer {
    val UPDATE_INTERVAL = 100L

    fun StartDiggingTimer(duration: Long, update: (Long) -> Unit, finish: () -> Unit): Subscription {
        val subscription = Observable.interval(0, UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    update(it * UPDATE_INTERVAL)

                    if (it * UPDATE_INTERVAL >= duration)
                        finish()
                }, {
                    Log.e("DiggingTimer", it.message)
                })

        return subscription!!
    }
}