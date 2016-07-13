package com.dtprogramming.treasurehuntirl.ui.recycler_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.impl.ClueConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
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
        lateinit var waypointCount: TextView
        lateinit var clueCount: TextView

        lateinit var treasureHunt: TreasureHunt

        init {
            titleText = view.treasure_hunt_view_holder_title
            waypointCount = view.treasure_hunt_view_holder_waypoint_count
            clueCount = view.treasure_hunt_view_holder_clue_count

            view.setOnClickListener {
                view.context.startActivity(CreateHuntActivity.getLoadIntent(view.context, treasureHunt.uuid))
            }
        }

        fun bind(treasureHunt: TreasureHunt) {
            this.treasureHunt = treasureHunt

            //TODO show how many treasure chest there are

            titleText.text = treasureHunt.title
            this.waypointCount.text = "$waypointCount Waypoints"
            this.clueCount.text = "$clueCount Clues"
        }
    }
}