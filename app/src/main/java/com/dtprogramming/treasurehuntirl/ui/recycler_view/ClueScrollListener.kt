package com.dtprogramming.treasurehuntirl.ui.recycler_view

import android.support.v7.widget.RecyclerView

/**
 * Created by ryantaylor on 6/20/16.
 */
class ClueScrollListener : RecyclerView.OnScrollListener() {
    var smoothScrolling = false
    var lastDx = 0

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            smoothScrolling = false

            if (recyclerView != null) {
                if (lastDx > 0) {
                    val layoutManager = recyclerView.layoutManager as CustomLinearLayoutManager

                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    recyclerView.smoothScrollToPosition(lastVisiblePosition)
                } else if (lastDx < 0) {
                    val layoutManager = recyclerView.layoutManager as CustomLinearLayoutManager

                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                    recyclerView.smoothScrollToPosition(firstVisiblePosition)
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        lastDx = dx

        if (!smoothScrolling && recyclerView != null && recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING && dx < 60 && dx > -60) {
            smoothScrolling = true

            if (dx > 0) {
                val layoutManager = recyclerView.layoutManager as CustomLinearLayoutManager

                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                recyclerView.smoothScrollToPosition(lastVisiblePosition)
            } else {
                val layoutManager = recyclerView.layoutManager as CustomLinearLayoutManager

                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                recyclerView.smoothScrollToPosition(firstVisiblePosition)
            }
        }
    }
}