package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.ViewTreasureHuntContainer
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import kotlinx.android.synthetic.main.activity_view_treasure_hunt.*

/**
 * Created by ryantaylor on 7/16/16.
 */
class ViewTreasureHuntActivity : ContainerActivity() {

    override lateinit var parent: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_treasure_hunt)

        parent = view_treasure_hunt_container

        if (intent.hasExtra(HUNT_UUID)) {
            val extras = Bundle()

            extras.putString(HUNT_UUID, intent.getStringExtra(HUNT_UUID))

            startContainer(ViewTreasureHuntContainer.URI, extras)
        }
    }

    override fun setToolBarTitle(title: String) {
        toolbar.title = title
    }
}