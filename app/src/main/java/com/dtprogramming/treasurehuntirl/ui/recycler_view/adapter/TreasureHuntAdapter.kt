package com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ListRecyclerViewSectionAdapter
import kotlinx.android.synthetic.main.view_holder_treasure_hunt.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 6/29/16.
 */
class TreasureHuntAdapter(items: List<TreasureHunt>, val itemSelected: (TreasureHunt) -> Unit) : ListRecyclerViewSectionAdapter<TreasureHuntAdapter.TreasureHuntViewHolder, TreasureHunt>(items) {

    override fun onCreateViewHolder(parent: ViewGroup?): TreasureHuntViewHolder? {
        return TreasureHuntViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_treasure_hunt, parent, false), itemSelected)
    }

    override fun onBindViewHolder(viewHolder: TreasureHuntViewHolder?, item: TreasureHunt?) {
        if (item != null)
            viewHolder?.bind(item)
    }

    override fun needsSectionBefore(item: TreasureHunt?): Boolean {
        return false
    }

    class TreasureHuntViewHolder(view: View, itemSelected: (TreasureHunt) -> Unit) : RecyclerView.ViewHolder(view) {

        @Inject
        lateinit var treasureChestConnection: TreasureChestConnection

        lateinit var titleText: TextView
        lateinit var chestCount: TextView

        lateinit var treasureHunt: TreasureHunt

        init {
            THApp.databaseComponent.inject(this)

            titleText = view.treasure_hunt_view_holder_title
            chestCount = view.treasure_hunt_view_holder_chest_count

            view.setOnClickListener {
                itemSelected(treasureHunt)
            }
        }

        fun bind(treasureHunt: TreasureHunt) {
            this.treasureHunt = treasureHunt

            treasureChestConnection.getTreasureChestCountForTreasureHuntAsync(treasureHunt.uuid, { count: Int ->
                if (count == 1)
                    chestCount.text = "$count Treasure Chest"
                else
                    chestCount.text = "$count Treasure Chests"
            })

            titleText.text = treasureHunt.title
        }
    }
}