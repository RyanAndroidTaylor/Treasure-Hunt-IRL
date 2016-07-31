package com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ListRecyclerViewSectionAdapter
import com.dtprogramming.treasurehuntirl.util.TEXT_CLUE
import kotlinx.android.synthetic.main.view_holder_clue.view.*

/**
 * Created by ryantaylor on 6/16/16.
 */
class ClueAdapter(context: Context, clues: List<Clue>, val itemSelected: (Clue) -> Unit) : ListRecyclerViewSectionAdapter<ClueAdapter.ClueViewHolder, Clue>(context, clues) {

    override fun needsSectionBefore(item: Clue?): Boolean {
        return false
    }

    override fun onBindViewHolder(viewHolder: ClueViewHolder?, clue: Clue?) {
        viewHolder?.bind(clue)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ClueViewHolder? {
        parent?.let {
            val view = LayoutInflater.from(it.context).inflate(R.layout.view_holder_clue, it, false)

            return ClueViewHolder(view, itemSelected)
        }

        return null
    }

    open class ClueViewHolder(val view: View, val itemSelected: (Clue) -> Unit) : RecyclerView.ViewHolder(view) {

        val clueText: TextView

        init {
            clueText = view.view_holder_clue_text
        }

        fun bind(clue: Clue?) {
            clue?.let {
                view.setOnClickListener { itemSelected(clue) }

                when (clue.type) {
                    TEXT_CLUE -> {
                        val textClue = clue as TextClue

                        clueText.text = textClue.text
                    }
                }
            }
        }
    }
}