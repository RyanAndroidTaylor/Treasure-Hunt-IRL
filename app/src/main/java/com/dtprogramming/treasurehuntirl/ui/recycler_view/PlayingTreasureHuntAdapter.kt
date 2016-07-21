package com.dtprogramming.treasurehuntirl.ui.recycler_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import kotlinx.android.synthetic.main.adapter_treasure_chest.view.*

/**
 * Created by ryantaylor on 7/21/16.
 */
class PlayingTreasureHuntAdapter(context: Context, playingTreasureHunts: List<PlayingTreasureHunt>, val selected: (PlayingTreasureHunt) -> Unit) : ListRecyclerViewSectionAdapter<PlayingTreasureHuntAdapter.PlayingTreasureHuntViewHolder, PlayingTreasureHunt>(context, playingTreasureHunts) {

    override fun onBindViewHolder(viewHolder: PlayingTreasureHuntViewHolder?, item: PlayingTreasureHunt) {
        viewHolder?.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): PlayingTreasureHuntViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_playing_treasure_hunt, parent, false)

        return PlayingTreasureHuntViewHolder(view, selected)
    }

    override fun needsSectionBefore(item: PlayingTreasureHunt?): Boolean {
        return false
    }

    class PlayingTreasureHuntViewHolder(val view: View, val selected: (PlayingTreasureHunt) -> Unit) : RecyclerView.ViewHolder(view) {

//        val title: TextView

        init {
//            title = view.adapter_treasure_chest_title
        }

        fun bind(playingTreasureHunt: PlayingTreasureHunt) {
            view.setOnClickListener { selected(playingTreasureHunt) }
        }
    }
}