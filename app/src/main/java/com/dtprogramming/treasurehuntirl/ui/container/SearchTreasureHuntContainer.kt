package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.activities.ViewTreasureHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.TreasureHuntAdapter
import com.dtprogramming.treasurehuntirl.util.TREASURE_HUNT_UUID
import kotlinx.android.synthetic.main.container_search_treasure_hunt_list.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 8/9/16.
 */
class SearchTreasureHuntContainer : BasicContainer() {

    companion object {
        val URI: String = SearchTreasureHuntContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_search_treasure_hunt_list

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: TreasureHuntAdapter

    @Inject
    lateinit var treasureHuntConnection: TreasureHuntConnection

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle) : Container {
        super.inflate(containerActivity, parent, extras)
        THApp.databaseComponent.inject(this)

        recyclerView = parent.fragment_hunt_list

        recyclerView.layoutManager = LinearLayoutManager(containerActivity)

        adapter = TreasureHuntAdapter(listOf(), { launchTreasureHuntActivity(it) })

        recyclerView.adapter = adapter

        treasureHuntConnection.getTreasureHuntsAsync { adapter.updateList(it) }

        return this
    }

    override fun onPause() {
        super.onPause()

        treasureHuntConnection.unsubscribe()
    }

    private fun launchTreasureHuntActivity(treasureHunt: TreasureHunt) {
        ViewTreasureHuntActivity.loadActivity(containerActivity, treasureHunt.uuid)

        containerActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}