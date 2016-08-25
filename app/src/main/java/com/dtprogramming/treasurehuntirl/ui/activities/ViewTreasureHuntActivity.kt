package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.ViewTreasureHuntContainer
import com.dtprogramming.treasurehuntirl.util.TREASURE_HUNT_UUID
import kotlinx.android.synthetic.main.activity_view_treasure_hunt.*

/**
 * Created by ryantaylor on 7/16/16.
 */
class ViewTreasureHuntActivity : ContainerActivity() {

    companion object {
        fun loadActivity(context: Context, treasureHuntUuid: String) {
            val intent = Intent(context, ViewTreasureHuntActivity::class.java)

            intent.putExtra(TREASURE_HUNT_UUID, treasureHuntUuid)

            context.startActivity(intent)
        }
    }

    override lateinit var parent: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_treasure_hunt)

        parent = view_treasure_hunt_container

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_URI)) {
            startContainer(savedInstanceState.getString(CURRENT_URI))
        } else if (intent.hasExtra(TREASURE_HUNT_UUID)) {
            val extras = Bundle()

            extras.putString(TREASURE_HUNT_UUID, intent.getStringExtra(TREASURE_HUNT_UUID))

            startContainer(ViewTreasureHuntContainer.URI, extras)
        }
    }

    override fun setToolBarTitle(title: String) {
        toolbar.title = title
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}