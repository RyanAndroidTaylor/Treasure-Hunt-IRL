package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CreateClueAdapter
import kotlinx.android.synthetic.main.activity_create_hunt.*

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

        create_hunt_clue_list.layoutManager = LinearLayoutManager(this)

        val adapter = CreateClueAdapter(this, listOf("Super good clue", "Vague clue", "More", "And", "More"))
        create_hunt_clue_list.adapter = adapter
    }
}