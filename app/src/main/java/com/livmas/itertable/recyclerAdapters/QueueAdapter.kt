package com.livmas.itertable.recyclerAdapters

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem
import java.util.LinkedList

class QueueAdapter(context: Context, dataModel: DataModel):
    ItemAdapter(LinkedList<ListItem>(), context, dataModel) {

    fun pop(): ListItem {
        val toReturn = getItem()
        dataSet.removeAt(0)
        return toReturn
    }
    fun getItem(): ListItem {
        return dataSet[0]
    }
}