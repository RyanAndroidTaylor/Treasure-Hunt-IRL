package com.dtprogramming.treasurehuntirl.ui.container

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

/**
 * Created by ryantaylor on 6/19/16.
 */
interface Container {
    fun inflate(activity: AppCompatActivity, parent: ViewGroup): Container
    fun onBackPressed(): Boolean
}