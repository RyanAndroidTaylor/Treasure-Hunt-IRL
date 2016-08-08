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
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.activities.ViewTreasureHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.TreasureHuntAdapter
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import kotlinx.android.synthetic.main.fragment_hunt_list.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 6/15/16.
 */
class TreasureHuntListFragment : TabFragment() {
    override val title = "HUNTS"

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: TreasureHuntAdapter

    @Inject
    lateinit var treasureHuntConnection: TreasureHuntConnection

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_hunt_list, container, false)
        THApp.databaseComponent.inject(this)

        if (view != null) {
            recyclerView  = view.fragment_hunt_list

            recyclerView.layoutManager = LinearLayoutManager(context)

            adapter = TreasureHuntAdapter(listOf(), { launchTreasureHuntActivity(it) })

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

        treasureHuntConnection.getTreasureHuntsAsync { adapter.updateList(it) }
    }

    private fun launchTreasureHuntActivity(treasureHunt: TreasureHunt) {
        val intent = Intent(context, ViewTreasureHuntActivity::class.java)

        intent.putExtra(HUNT_UUID, treasureHunt.uuid)

        startActivity(intent)
    }
}