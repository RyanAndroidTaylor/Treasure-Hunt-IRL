package com.dtprogramming.treasurehuntirl.ui.container.animation

import android.app.Activity
import android.graphics.Point

/**
 * Created by ryantaylor on 8/25/16.
 */

fun inRight(activity: Activity): ContainerAnimator<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return FloatAnimator("x", screenSize.x.toFloat(), 0f)
}

fun outLeft(activity: Activity): ContainerAnimator<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return FloatAnimator("x", 0f, -screenSize.x.toFloat())
}

fun inLeft(activity: Activity): ContainerAnimator<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return FloatAnimator("x", -screenSize.x.toFloat(), 0f)
}

fun outRight(activity: Activity): ContainerAnimator<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return FloatAnimator("x", 0f, screenSize.x.toFloat())
}

fun circleReveal(centerX: Int, centerY: Int): ContainerAnimator<Int> {
    return CircularAnimator(centerX, centerY)
}