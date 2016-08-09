package com.dtprogramming.treasurehuntirl.ui.fragments

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
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.TreasureHuntAdapter
import kotlinx.android.synthetic.main.fragment_create_hunt.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntTabFragment : TabFragment() {
    override val title = "CREATE"

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: TreasureHuntAdapter

    @Inject
    lateinit var treasureHuntConnection: TreasureHuntConnection

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_create_hunt, container, false)

        if (view != null) {
            recyclerView = view.create_hunt_list

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TreasureHuntAdapter(listOf(), { itemSelected(it) })
            recyclerView.adapter = adapter

            view.create_hunt_fragment_fab?.setOnClickListener { startActivity(CreateHuntActivity.getCreateNewIntent(activity)) }
        }

        return view
    }

    override fun onPause() {
        super.onPause()

        treasureHuntConnection.unsubscribe()
    }

    override fun onResume() {
        super.onResume()

        treasureHuntConnection.subscribeToTreasureHunts { adapter.updateList(it) }
    }

    fun itemSelected(treasureHunt: TreasureHunt) {
        startActivity(CreateHuntActivity.getLoadIntent(context, treasureHunt.uuid))
    }
}