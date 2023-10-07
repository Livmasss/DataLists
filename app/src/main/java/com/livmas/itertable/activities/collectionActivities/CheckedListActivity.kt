package com.livmas.itertable.activities.collectionActivities

import android.os.Bundle
import com.livmas.itertable.activities.SimpleCollectionActivity
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import com.livmas.itertable.recyclerAdapters.collections.CheckedListAdapter

class CheckedListActivity: SimpleCollectionActivity() {
    override lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = CheckedListAdapter(this, dataModel)
        super.onCreate(savedInstanceState)
    }
}