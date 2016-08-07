package com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ListRecyclerViewSectionAdapter
import kotlinx.android.synthetic.main.adapter_treasure_chest.view.*

/**
 * Created by ryantaylor on 7/11/16.
 */
class TreasureChestAdapter(val onItemClickListener: (TreasureChest) -> Unit, treasureChest: List<TreasureChest>): ListRecyclerViewSectionAdapter<TreasureChestAdapter.TreasureChestViewHolder, TreasureChest>(treasureChest) {
    override fun onBindViewHolder(viewHolder: TreasureChestViewHolder?, item: TreasureChest?) {
        viewHolder?.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): TreasureChestViewHolder? {
        parent?.let {
            val view = LayoutInflater.from(it.context).inflate(R.layout.adapter_treasure_chest, it, false)

            return TreasureChestViewHolder(onItemClickListener, view)
        }

        return null
    }

    override fun needsSectionBefore(item: TreasureChest?): Boolean {
        return false
    }

    class TreasureChestViewHolder(val onItemClickListener: (TreasureChest) -> Unit, view: View): RecyclerView.ViewHolder(view) {

        private var treasureChest: TreasureChest? = null

        val treasureChestTitle: TextView

        init {
            treasureChestTitle = view.adapter_treasure_chest_title

            view.setOnClickListener {
                treasureChest?.let { onItemClickListener(it) }
            }
        }

        fun bind(treasureChest: TreasureChest?) {
            this.treasureChest = treasureChest

            treasureChest?.let {
                treasureChestTitle.text = treasureChest.title
            }
        }
    }
}