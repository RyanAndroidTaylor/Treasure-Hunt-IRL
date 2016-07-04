package com.dtprogramming.treasurehuntirl.ui.recycler_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import kotlinx.android.synthetic.main.view_holder_treasure_hunt.view.*

/**
 * Created by ryantaylor on 6/29/16.
 */
class TreasureHuntAdapter(context: Context, items: List<TreasureHunt>) : ListRecyclerViewSectionAdapter<TreasureHuntAdapter.TreasureHuntViewHolder, TreasureHunt>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup?): TreasureHuntViewHolder? {
        return TreasureHuntViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.view_holder_treasure_hunt, parent, false))
    }

    override fun onBindViewHolder(viewHolder: TreasureHuntViewHolder?, item: TreasureHunt?) {
        if (item != null)
            viewHolder?.bind(item)
    }

    override fun needsSectionBefore(item: TreasureHunt?): Boolean {
        return false
    }

    class TreasureHuntViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var titleText: TextView

        init {
            titleText = view.treasure_hunt_view_holder_title
        }

        fun bind(treasureHunt: TreasureHunt) {
            titleText.text = treasureHunt.title
        }
    }
}