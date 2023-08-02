package com.livmas.itertable.recyclerAdapters.collections

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import java.util.LinkedList

class ListAdapter(context: Context, dataModel: DataModel):
        ItemAdapter(LinkedList<ListItem>(), context, dataModel){
        override fun pop(): ListItem? {
                return getItem()
        }

        override fun getItem(): ListItem? {
                if (isEmpty())
                        return null

                return dataSet[0]
        }
}