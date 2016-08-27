package com.dtprogramming.treasurehuntirl.ui.container.animation

import android.animation.Animator
import android.animation.ObjectAnimator

/**
 * Created by ryantaylor on 8/26/16.
 */
class FloatAnimator(val value: String, from: Float, to: Float) : ContainerAnimator<Float>(from, to) {

    override fun getAnimator(item: Any): Animator {
        return ObjectAnimator.ofFloat(item, value, from, to)
    }

}