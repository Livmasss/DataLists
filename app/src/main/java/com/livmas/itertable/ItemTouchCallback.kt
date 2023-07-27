package com.livmas.itertable

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.recyclerAdapters.ItemAdapter

class ItemTouchCallback(private val adapter: ItemAdapter): ItemTouchHelper.Callback() {
    var holder: RecyclerView.ViewHolder? = null
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN or ItemTouchHelper.UP)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != target.itemViewType)
            return false

        val from = viewHolder.absoluteAdapterPosition
        val to = target.absoluteAdapterPosition

        adapter.apply {
            swap(from, to)
            notifyItemChanged(from)
        }

        viewHolder.itemView.findViewById<TextView>(R.id.tvNumber).visibility = View.INVISIBLE
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                holder = viewHolder
            }
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            holder?.let {
                adapter.notifyItemChanged(it.absoluteAdapterPosition)
                it.itemView.findViewById<TextView>(R.id.tvNumber).visibility = View.VISIBLE
            }
        }
    }
}