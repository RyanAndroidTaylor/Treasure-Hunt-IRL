package com.dtprogramming.treasurehuntirl.ui.container

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.PlayingTreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.ui.activities.BottomTabActivity
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.activities.PlayTreasureHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.PlayingTreasureHuntAdapter
import com.dtprogramming.treasurehuntirl.util.PLAYING_HUNT_UUID
import kotlinx.android.synthetic.main.fragment_current_hunts.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 8/9/16.
 */
class PlayTreasureHuntListContainer : BasicContainer() {

    companion object {
        val URI: String = PlayTreasureHuntListContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.fragment_current_hunts

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PlayingTreasureHuntAdapter

    @Inject
    lateinit var treasureHuntConnection: PlayingTreasureHuntConnection

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        THApp.databaseComponent.inject(this)

        recyclerView = parent.current_hunt_list

        recyclerView.layoutManager = LinearLayoutManager(containerActivity)

        adapter = PlayingTreasureHuntAdapter(listOf(), { launchTreasureHuntActivity(it) })

        recyclerView.adapter = adapter

        treasureHuntConnection.getPlayingTreasureHuntsAsync { adapter.updateList(it) }

        return this
    }

    override fun onPause() {
        super.onPause()

        treasureHuntConnection.unsubscribe()
    }

    private fun launchTreasureHuntActivity(playingTreasureHunt: PlayingTreasureHunt) {
        val intent = Intent(containerActivity, PlayTreasureHuntActivity::class.java)

        intent.putExtra(PLAYING_HUNT_UUID, playingTreasureHunt.uuid)

        containerActivity.startActivity(intent)
    }
}