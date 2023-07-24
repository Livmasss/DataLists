package com.livmas.itertable.recyclerAdapters.collections

import android.content.Context
import com.livmas.itertable.DataModel
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import java.util.LinkedList

class ListAdapter(context: Context, dataModel: DataModel):
        ItemAdapter(LinkedList<ListItem>(), context, dataModel){
}