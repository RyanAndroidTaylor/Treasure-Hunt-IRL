package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.PlayTreasureHuntContainer
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import kotlinx.android.synthetic.main.activity_play_treasure_hunt.*

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntActivity : ContainerActivity() {
    override lateinit var parent: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_treasure_hunt)

        parent = play_treasure_hunt_container

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_URI)) {
            startContainer(savedInstanceState.getString(CURRENT_URI))
        } else if (intent.hasExtra(HUNT_UUID)) {
            val extras = Bundle()

            extras.putString(HUNT_UUID, intent.getStringExtra(HUNT_UUID))

            startContainer(PlayTreasureHuntContainer.URI, extras)
        }
    }

    override fun setToolBarTitle(title: String) {
        toolbar.title = title
    }
}