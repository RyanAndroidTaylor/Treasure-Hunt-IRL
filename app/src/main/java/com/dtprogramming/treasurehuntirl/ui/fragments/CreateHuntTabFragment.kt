package com.dtprogramming.treasurehuntirl.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.DatabaseObservables
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureHuntAdapter
import kotlinx.android.synthetic.main.fragment_create_hunt.view.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntTabFragment : TabFragment() {
    override val title = "CREATE"

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: TreasureHuntAdapter

    var treasureHuntConnection = TreasureHuntConnectionImpl()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_create_hunt, container, false)

        if (view != null) {
            recyclerView = view.create_hunt_list

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TreasureHuntAdapter(context, listOf())
            recyclerView.adapter = adapter

            treasureHuntConnection.getTreasureHuntsAsync { adapter.updateList(it) }

            view.create_hunt_fragment_fab?.setOnClickListener { startActivity(CreateHuntActivity.getCreateNewIntent(activity)) }
        }

        return view
    }
}