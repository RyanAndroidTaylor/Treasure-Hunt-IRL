package com.dtprogramming.treasurehuntirl.ui.container

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * Created by ryantaylor on 6/19/16.
 */
interface Container {

    var layout: View

    fun inflate(parent: ViewGroup): Container
    fun onBackPressed(): Boolean

    fun animateIn(context: Context, animationId: Int) {
        val animation = AnimationUtils.loadAnimation(context, animationId)
        animation.duration = 500
        layout.startAnimation(animation)
    }

    fun animateOut(context: Context, animationId: Int) {
        val animation = AnimationUtils.loadAnimation(context, animationId)
        animation.duration = 500

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                (layout.parent as ViewGroup).removeView(layout)
            }

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })

        layout.startAnimation(animation)
    }
}