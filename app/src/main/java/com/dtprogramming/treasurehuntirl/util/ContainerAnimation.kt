package com.dtprogramming.treasurehuntirl.util

import android.animation.ObjectAnimator

/**
 * Created by ryantaylor on 8/25/16.
 */
class ContainerAnimation<out T>(val value: String, val from: T, val to: T) {
    var duration = 300L

    fun getAnimator(item: Any): ObjectAnimator {
        when  {
            from is Int && to is Int -> {
                return ObjectAnimator.ofInt(item, value, from, to).setDuration(duration)
            }
            from is Float && to is Float -> {
                return ObjectAnimator.ofFloat(item, value, from, to)
            }
            else -> {
                throw RuntimeException("Type not supported. from $from, to $to")
            }
        }
    }

    fun setDuration(duration: Long): ContainerAnimation<T> {
        this.duration = duration

        return this
    }
}