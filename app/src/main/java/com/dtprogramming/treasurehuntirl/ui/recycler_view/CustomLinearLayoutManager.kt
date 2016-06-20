package com.dtprogramming.treasurehuntirl.ui.recycler_view

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics

/**
 * Created by ryantaylor on 6/18/16.
 */
class CustomLinearLayoutManager(val context: Context, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {

    var scrollSpeed = 0f

    constructor(context: Context): this(context, VERTICAL, false)



    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = object : LinearSmoothScroller(context) {
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                val xDelta = calculateDxToMakeVisible(recyclerView!!.getChildAt(position), SNAP_TO_ANY)

                return PointF(xDelta.toFloat(), 0f)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return 0.1f
            }
        }

        linearSmoothScroller.targetPosition = position

        startSmoothScroll(linearSmoothScroller)
    }
}