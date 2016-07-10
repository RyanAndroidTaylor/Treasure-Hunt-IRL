package com.dtprogramming.treasurehuntirl.ui

import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.GeneralClickAction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Tap

/**
 * Created by ryantaylor on 7/10/16.
 */
object Actions {

    fun clickCenterLeft(): ViewAction {
        return GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER)
    }

    fun clickCenterRight(): ViewAction {
        return GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER)
    }
}