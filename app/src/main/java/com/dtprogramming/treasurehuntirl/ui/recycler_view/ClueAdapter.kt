package com.dtprogramming.treasurehuntirl.ui.recycler_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.Clue
import kotlinx.android.synthetic.main.view_holder_clue.view.*

/**
 * Created by ryantaylor on 6/16/16.
 */
class ClueAdapter(context: Context, clues: List<Clue>) : ListRecyclerViewSectionAdapter<RecyclerView.ViewHolder, Clue>(context, clues) {

    override fun needsSectionBefore(item: Clue?): Boolean {
        return false
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, clue: Clue?) {
        if (viewHolder is ClueViewHolder) {
            clue?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ClueViewHolder? {
        parent?.let {
            val view = LayoutInflater.from(it.context).inflate(R.layout.view_holder_clue, it, false)

            return ClueViewHolder(view)
        }

        return null
    }

    open class ClueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clueText: TextView

        init {
            clueText = view.view_holder_clue_text
        }

        fun bind(clue: Clue) {
            clueText.text = clue.text
        }
    }
}