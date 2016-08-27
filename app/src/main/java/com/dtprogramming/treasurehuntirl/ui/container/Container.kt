package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity

/**
 * Created by ryantaylor on 6/19/16.
 */
interface Container {

    fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): View

    fun onPause()
    fun onReload(parent: ViewGroup)
    fun onFinish()
}