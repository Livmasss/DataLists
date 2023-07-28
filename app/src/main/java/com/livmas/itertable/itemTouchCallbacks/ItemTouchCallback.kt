package com.livmas.itertable.itemTouchCallbacks

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.R
import com.livmas.itertable.recyclerAdapters.ItemAdapter

class ItemTouchCallback(private val adapter: ItemAdapter): BasicTouchCallback() {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (!super.onMove(recyclerView, viewHolder, target))
            return false

        val from = viewHolder.absoluteAdapterPosition
        val to = target.absoluteAdapterPosition

        adapter.apply {
            swap(from, to)
            notifyItemChanged(from)
        }
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            holder?.let {
                adapter.notifyItemChanged(it.absoluteAdapterPosition)
                it.itemView.findViewById<TextView>(R.id.tvNumber).visibility = View.VISIBLE
            }
        }
    }
}