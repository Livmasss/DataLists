package com.livmas.itertable.recyclerAdapters.collections

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import java.util.LinkedList

open class QueueAdapter(context: Context, dataModel: DataModel):
    ItemAdapter(LinkedList<ListItem>(), context, dataModel) {

    override fun pop(): ListItem? {
        if (isEmpty())
            return null
        val toReturn = getItem()
        onDeleteClickListener(0)
        return toReturn
    }
    override fun getItem(): ListItem {
        return dataSet[0]
    }
}