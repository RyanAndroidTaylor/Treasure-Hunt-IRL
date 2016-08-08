package com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.CollectedClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.impl.CollectedClueConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ListRecyclerViewSectionAdapter
import com.dtprogramming.treasurehuntirl.util.*
import kotlinx.android.synthetic.main.adapter_inventory_item.view.*
import javax.inject.Inject

/**
 * Created by ryantaylor on 7/26/16.
 */
class InventoryAdapter(items: List<InventoryItem>, val itemSelected: (item: InventoryItem) -> Unit) : ListRecyclerViewSectionAdapter<InventoryAdapter.InventoryViewHolder, InventoryItem>(items) {

    override fun needsSectionBefore(item: InventoryItem?): Boolean {
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_inventory_item, parent, false)

        return InventoryViewHolder(view, itemSelected)
    }

    override fun onBindViewHolder(viewHolder: InventoryViewHolder?, item: InventoryItem?) {
        viewHolder?.bind(item)
    }


    class InventoryViewHolder(val view: View, val itemSelected: (item: InventoryItem) -> Unit) : RecyclerView.ViewHolder(view) {

        private lateinit var itemInfo: TextView

        @Inject
        lateinit var collectedClueConnection: CollectedClueConnection

        init {
            THApp.databaseComponent.inject(this)
        }

        fun bind(item: InventoryItem?) {
            itemInfo = view.adapter_inventory_item_info

            item?.let {
                view.setOnClickListener { itemSelected(item) }

                itemInfo.setCompoundDrawablesWithIntrinsicBounds(item.iconDrawable, 0, 0, 0)

                when (item.type) {
                    TEXT_CLUE ->  {
                        val clue = collectedClueConnection.getCollectedClue(item.uuid)

                        itemInfo.text = clue.text
                    }
                    WAYPOINT -> {
                        itemInfo.text = "WAYPOINT"
                    }
                    TREASURE_CHEST -> {
                        val chest = item as CollectedTreasureChest

                        if (chest.state == LOCKED) {
                            itemInfo.text = "Locked Treasure Chest"
                        } else if (chest.state == CLOSED) {
                            itemInfo.text = "Closed Treasure Chest"
                        }
                    }
                }
            }
        }
    }
}