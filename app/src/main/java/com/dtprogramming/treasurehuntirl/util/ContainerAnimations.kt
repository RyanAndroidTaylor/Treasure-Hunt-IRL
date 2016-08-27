package com.dtprogramming.treasurehuntirl.util

import android.app.Activity
import android.graphics.Point

/**
 * Created by ryantaylor on 8/25/16.
 */

fun inRight(activity: Activity): ContainerAnimation<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return ContainerAnimation("x", screenSize.x.toFloat(), 0f)
}

fun outLeft(activity: Activity): ContainerAnimation<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return ContainerAnimation("x", 0f, -screenSize.x.toFloat())
}

fun inLeft(activity: Activity): ContainerAnimation<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return ContainerAnimation("x", -screenSize.x.toFloat(), 0f)
}

fun outRight(activity: Activity): ContainerAnimation<Float> {
    val screenSize = Point()

    activity.windowManager.defaultDisplay.getRealSize(screenSize)

    return ContainerAnimation("x", 0f, screenSize.x.toFloat())
}