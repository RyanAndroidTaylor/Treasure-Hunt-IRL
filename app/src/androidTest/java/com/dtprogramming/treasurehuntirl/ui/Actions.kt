package com.dtprogramming.treasurehuntirl.ui

import android.graphics.Rect
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.GeneralClickAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Tap
import android.view.View

/**
 * Created by ryantaylor on 7/10/16.
 */
object Actions {

    fun clickCenterLeft(): ViewAction {
        return GeneralClickAction(Tap.SINGLE, AdjustableValueViewLeftDrawable(), Press.FINGER)
    }

    fun clickCenterRight(): ViewAction {
        return GeneralClickAction(Tap.SINGLE, AdjustableValueViewRightDrawable(), Press.FINGER)
    }

    class AdjustableValueViewLeftDrawable : CoordinatesProvider {
        override fun calculateCoordinates(view: View?): FloatArray {
            val xy = IntArray(2)

            view!!.getLocationOnScreen(xy)

            val visibleParts = Rect()
            view.getGlobalVisibleRect(visibleParts)

            val x = xy[0] + 20.0f
            val y = xy[1] + (visibleParts.height() - 1) / 2.0f

            return floatArrayOf(x, y)
        }
    }

    class AdjustableValueViewRightDrawable : CoordinatesProvider {
        override fun calculateCoordinates(view: View?): FloatArray {
            val xy = IntArray(2)

            view!!.getLocationOnScreen(xy)

            val visibleParts = Rect()
            view.getGlobalVisibleRect(visibleParts)

            val x = xy[0] + visibleParts.width() - 20.0f
            val y = xy[1] + (visibleParts.height() - 1) / 2.0f

            return floatArrayOf(x, y)
        }
    }
}