package com.livmas.itertable

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.recyclerAdapters.ItemAdapter

class ItemTouchCallback(private val adapter: ItemAdapter): ItemTouchHelper.Callback() {
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

        if (from < 0 || to < 0) {
            return false
        }
        if (from > adapter.itemCount || to > adapter.itemCount) {
            return false
        }

        val item = adapter.at(from)

        adapter.apply {
            remove(from)
            insert(to, item)

            val min = Integer.min(from, to)
            val max = Integer.max(from, to)

            updateRangeNumbers(min, max+1)
        }

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}