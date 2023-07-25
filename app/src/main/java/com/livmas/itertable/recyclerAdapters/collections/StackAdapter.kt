package com.livmas.itertable.recyclerAdapters.collections

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem

class StackAdapter(context: Context, dataModel: DataModel):
    QueueAdapter(context, dataModel) {
    override fun pop(): ListItem {
        val item = getItem()
        onDeleteClickListener(0)
        return item
    }

    override fun getItem(): ListItem {
        return dataSet[0]
    }

    override fun remove(position: Int) {
        dataSet.removeAt(position)

        for (i in 0 until position) {
            val item = dataSet[i]
            item.number--

            Thread {
                db.getDao().updateItem(item)
            }.start()
        }

        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun add(item: ListItem) {
        dataSet.add(0, item)
        notifyDataSetChanged()
    }
}