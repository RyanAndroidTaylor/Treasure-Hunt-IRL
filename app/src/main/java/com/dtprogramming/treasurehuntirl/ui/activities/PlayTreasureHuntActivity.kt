package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.PlayTreasureHuntContainer
import com.dtprogramming.treasurehuntirl.util.NEW
import com.dtprogramming.treasurehuntirl.util.PLAYING_HUNT_UUID
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
        } else if (intent.hasExtra(PLAYING_HUNT_UUID)) {
            val extras = Bundle()

            extras.putString(PLAYING_HUNT_UUID, intent.getStringExtra(PLAYING_HUNT_UUID))

            if (intent.hasExtra(NEW))
                extras.putBoolean(NEW, true)

            startContainer(PlayTreasureHuntContainer.URI, extras)
        }
    }

    override fun setToolBarTitle(title: String) {
        toolbar.title = title
    }
}