package com.dtprogramming.treasurehuntirl.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureHuntAdapter
import kotlinx.android.synthetic.main.fragment_hunt_list.view.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class TreasureHuntListFragment : TabFragment() {
    override val title = "HUNTS"

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: TreasureHuntAdapter

    private val treasureHuntConnection = TreasureHuntConnectionImpl()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_hunt_list, container, false)

        if (view != null) {
            recyclerView  = view.fragment_hunt_list

            recyclerView.layoutManager = LinearLayoutManager(context)

            //TODO Setup view treasure hunt activity and launch it when a treasure hunt is selected
            adapter = TreasureHuntAdapter(context, listOf(), {  })

            recyclerView.adapter = adapter

            treasureHuntConnection.getTreasureHuntsAsync { adapter.updateList(it) }
        }

        return view
    }
}