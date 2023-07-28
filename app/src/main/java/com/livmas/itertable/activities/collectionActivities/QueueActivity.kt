package com.livmas.itertable.activities.collectionActivities

import android.os.Bundle
import android.widget.Toast
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import com.livmas.itertable.recyclerAdapters.collections.QueueAdapter

open class QueueActivity : ComplexCollectionActivity() {
    override lateinit var adapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = QueueAdapter(this, dataModel)
        super.onCreate(savedInstanceState)

        binding.bPop.setOnClickListener {
            val item = adapter.pop()

            Toast.makeText(this, item.name, Toast.LENGTH_SHORT).show()
        }
    }
}