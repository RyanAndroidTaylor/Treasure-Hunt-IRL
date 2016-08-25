package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.activities.CreateTreasureHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.TreasureHuntAdapter
import kotlinx.android.synthetic.main.container_create_hunt_list.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 8/9/16.
 */
class CreateTreasureHuntListContainer() : BasicContainer() {

    companion object {
        val URI: String = CreateTreasureHuntListContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_create_hunt_list

    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView

    lateinit var adapter: TreasureHuntAdapter

    @Inject
    lateinit var treasureHuntConnection: TreasureHuntConnection

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        THApp.databaseComponent.inject(this)

        recyclerView = parent.create_hunt_list

        recyclerView.layoutManager = LinearLayoutManager(containerActivity, LinearLayoutManager.VERTICAL, false)
        adapter = TreasureHuntAdapter(listOf(), { itemSelected(it) })
        recyclerView.adapter = adapter

        fab = parent.create_hunt_fragment_fab
        fab.setOnClickListener { createNewTreasureHunt() }

        treasureHuntConnection.subscribeToTreasureHunts { adapter.updateList(it) }

        return this
    }

    override fun onPause() {
        super.onPause()

        treasureHuntConnection.unsubscribe()
    }

    fun itemSelected(treasureHunt: TreasureHunt) {
        CreateTreasureHuntActivity.loadActivity(containerActivity, treasureHunt.uuid)
        containerActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun createNewTreasureHunt() {
        CreateTreasureHuntActivity.startNewActivity(containerActivity)
        containerActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}