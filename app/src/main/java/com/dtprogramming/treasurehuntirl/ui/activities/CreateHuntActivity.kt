package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.fragments.CreateClueFragment
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CustomLinearLayoutManager
import kotlinx.android.synthetic.main.activity_create_hunt.*
import java.util.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CreateHuntActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hunt)

        toolbar?.title = resources.getString(R.string.create_hunt_activity_title)

        create_hunt_clue_list.layoutManager = CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val clues = ArrayList<String>()

        for (i in 0..100)
            clues.add("This is a clue to a sweet treasure hunt that only a few people will ever figure out. That is how good it is. We need a lot more text in here so it looks good to the eye when we are playing with in on the screen")

        val adapter = ClueAdapter(this, clues)

        create_hunt_clue_list.adapter = adapter

        create_hunt_add_clue.setOnClickListener {
            val createClueFragment = CreateClueFragment()

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(android.R.id.content, createClueFragment)
            transaction.commit()
        }

        create_hunt_clue_list.addOnScrollListener(SmoothScrollListener())
    }

    class SmoothScrollListener : RecyclerView.OnScrollListener() {
        var smoothScrolling = false

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                smoothScrolling = false
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (!smoothScrolling && recyclerView != null && recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING && dx < 60 && dx > -60) {
                smoothScrolling = true

                if (dx > 0) {
                    val layoutManager = recyclerView.layoutManager as CustomLinearLayoutManager

                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    layoutManager.scrollSpeed = dx.toFloat()
                    recyclerView.smoothScrollToPosition(lastVisiblePosition)
                } else {
                    val layoutManager = recyclerView.layoutManager as CustomLinearLayoutManager

                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                    layoutManager.scrollSpeed = dx.toFloat()
                    recyclerView.smoothScrollToPosition(firstVisiblePosition)
                }
            }
        }
    }
}