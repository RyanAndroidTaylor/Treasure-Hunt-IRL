package com.dtprogramming.treasurehuntirl.ui.container.animation

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils

/**
 * Created by ryantaylor on 8/26/16.
 */
class CircularAnimator(centerX: Int, centerY: Int) : ContainerAnimator<Int>(centerX, centerY) {

    override fun getAnimator(item: Any): Animator {
        val view = item as View

        return ViewAnimationUtils.createCircularReveal(view, from, to, 0f, Math.hypot(view.width.toDouble(), view.height.toDouble()).toFloat())
    }
}