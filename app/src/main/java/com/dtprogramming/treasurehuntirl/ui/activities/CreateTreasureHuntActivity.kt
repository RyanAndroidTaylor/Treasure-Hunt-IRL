package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.CreateTreasureHuntContainer
import com.dtprogramming.treasurehuntirl.util.TREASURE_HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import kotlinx.android.synthetic.main.activity_create_hunt.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateTreasureHuntActivity : ContainerActivity() {

    override lateinit var parent: ViewGroup

    companion object {
        fun startNewActivity(context: Context) {
            val intent = Intent(context, CreateTreasureHuntActivity::class.java)

            intent.putExtra(NEW, true)

            context.startActivity(intent)
        }

        fun loadActivity(context: Context, treasureHuntId: String) {
            val intent = Intent(context, CreateTreasureHuntActivity::class.java)

            intent.putExtra(TREASURE_HUNT_UUID, treasureHuntId)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hunt)

        parent = activity_create_hunt_container

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_URI)) {
            startContainer(savedInstanceState.getString(CURRENT_URI))
        } else {
            if (intent.hasExtra(TREASURE_HUNT_UUID))
                CreateTreasureHuntContainer.loadExistingHunt(this, intent.getStringExtra(TREASURE_HUNT_UUID))
            if (intent.hasExtra(NEW))
                CreateTreasureHuntContainer.createNewHunt(this)
        }

        toolbar?.title = stringFrom(R.string.treasure_hunt_action_bar_title)
    }

    override fun setToolBarTitle(title: String) {
        toolbar?.title = title
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}