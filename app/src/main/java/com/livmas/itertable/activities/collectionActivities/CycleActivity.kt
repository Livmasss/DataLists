package com.livmas.itertable.activities.collectionActivities

import android.os.Bundle
import android.widget.Toast
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import com.livmas.itertable.recyclerAdapters.collections.CycleAdapter

class CycleActivity: ComplexCollectionActivity() {
    override lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = CycleAdapter(this, dataModel)
        super.onCreate(savedInstanceState)

        binding.bPop.setOnClickListener {
            adapter.apply {
                val item = pop()
                item.number = itemCount

                updateNumbers()
                notifyItemRangeChanged(0, itemCount-1)

                Toast.makeText(this@CycleActivity, item.name, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
