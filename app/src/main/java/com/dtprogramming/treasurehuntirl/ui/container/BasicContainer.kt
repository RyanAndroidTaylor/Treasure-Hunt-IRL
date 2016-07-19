package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity

/**
 * Created by ryantaylor on 6/20/16.
 */
abstract class BasicContainer : Container {

    abstract protected var rootViewId: Int
    private var rootView: View? = null

    protected lateinit var containerActivity: ContainerActivity
    protected lateinit var parent: ViewGroup
    private lateinit var extras: Bundle

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        this.containerActivity = containerActivity
        this.parent = parent
        this.extras = extras

        if (parent.childCount > 0)
            parent.removeViewAt(0)

        rootView = LayoutInflater.from(parent.context).inflate(rootViewId, parent, false)

        parent.addView(rootView)

        return this
    }

    override fun onPause() {

    }

    override fun onReload(parent: ViewGroup) {
        if (parent.childCount > 0)
            parent.removeViewAt(0)

        if (rootView == null) {
            rootView = LayoutInflater.from(parent.context).inflate(rootViewId, parent, false)
        } else {
            parent.addView(rootView)
        }
    }

    override fun onFinish() {

    }
}