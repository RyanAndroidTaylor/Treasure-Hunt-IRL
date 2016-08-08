package com.dtprogramming.treasurehuntirl.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.PlayingTreasureHuntConnection
import com.dtprogramming.treasurehuntirl.ui.activities.PlayTreasureHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.PlayingTreasureHuntAdapter
import com.dtprogramming.treasurehuntirl.database.connections.impl.PlayingTreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.util.PLAYING_HUNT_UUID
import kotlinx.android.synthetic.main.fragment_current_hunts.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 6/15/16.
 */
class CurrentHuntsFragment : TabFragment() {
    override val title = "IN PROGRESS"

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PlayingTreasureHuntAdapter

    @Inject
    lateinit var treasureHuntConnection: PlayingTreasureHuntConnection

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_current_hunts, container, false)
        THApp.databaseComponent.inject(this)

        view?.let {
            recyclerView = view.current_hunt_list

            recyclerView.layoutManager = LinearLayoutManager(context)

            adapter = PlayingTreasureHuntAdapter(listOf(), { launchTreasureHuntActivity(it) })

            recyclerView.adapter = adapter
        }

        return view
    }

    override fun onPause() {
        super.onPause()

        treasureHuntConnection.unsubscribe()
    }

    override fun onResume() {
        super.onResume()

        treasureHuntConnection.getPlayingTreasureHuntsAsync { adapter.updateList(it) }
    }

    private fun launchTreasureHuntActivity(playingTreasureHunt: PlayingTreasureHunt) {
        val intent = Intent(context, PlayTreasureHuntActivity::class.java)

        intent.putExtra(PLAYING_HUNT_UUID, playingTreasureHunt.uuid)

        startActivity(intent)
    }
}