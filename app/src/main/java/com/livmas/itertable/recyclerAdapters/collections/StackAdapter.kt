package com.livmas.itertable.recyclerAdapters.collections

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem

class StackAdapter(context: Context, dataModel: DataModel):
    QueueAdapter(context, dataModel) {
    override fun pop(): ListItem? {
        if (isEmpty())
            return null

        val item = getItem()
        onDeleteClickListener(0)
        return item
    }

    override fun getItem(): ListItem {
        return dataSet[0]
    }

    override fun notifiedRemove(position: Int) {
        remove(position)

        notifyItemRemoved(position)
        updateRangeNumbers(0, position)
        notifyItemRangeChanged(0, position)
    }

    override fun add(item: ListItem) {
        dataSet.add(0, item)
        notifyItemRangeChanged(0, itemCount)
    }

    override fun updateNumber(position: Int) {
        dataSet[position].number = itemCount - position
    }
}