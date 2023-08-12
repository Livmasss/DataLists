package com.livmas.itertable.recyclerAdapters.collections

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem

class CycleAdapter(context: Context, dataModel: DataModel): QueueAdapter(context, dataModel) {
    override fun pop(): ListItem? {
        if (isEmpty())
            return null

        val item = getItem()

        notifiedAdd(item)
        notifiedRemove(0)

        return item
    }
}