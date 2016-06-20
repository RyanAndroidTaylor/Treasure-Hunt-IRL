package com.dtprogramming.treasurehuntirl.ui.container

import android.view.ViewGroup

/**
 * Created by ryantaylor on 6/19/16.
 */
interface Container {

    fun inflate(parent: ViewGroup): Container
    fun replaceAndInflate(parent: ViewGroup): Container
}