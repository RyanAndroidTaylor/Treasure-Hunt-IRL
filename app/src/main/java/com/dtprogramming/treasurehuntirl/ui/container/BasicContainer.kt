package com.dtprogramming.treasurehuntirl.ui.container

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by ryantaylor on 6/20/16.
 */
abstract class BasicContainer : Container {

    fun inflate(parent: ViewGroup, layoutId: Int): Container {
        if (parent.childCount > 0)
            parent.removeViewAt(0)

        LayoutInflater.from(parent.context).inflate(layoutId, parent, true)

        loadViews(parent)

        return this
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    abstract protected fun loadViews(parent: ViewGroup)
}