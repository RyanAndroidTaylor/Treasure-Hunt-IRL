package com.dtprogramming.treasurehuntirl.ui.container.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by ryantaylor on 8/25/16.
 */
abstract class ContainerAnimator<out T>(val from: T, val to: T) {
    var duration = 300L

    abstract fun getAnimator(item: Any): Animator

    fun setDuration(duration: Long): ContainerAnimator<T> {
        this.duration = duration

        return this
    }
}