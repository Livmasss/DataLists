package com.livmas.itertable.activities.collectionActivities

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.recyclerAdapters.ItemAdapter
import com.livmas.itertable.recyclerAdapters.collections.StackAdapter

class StackActivity : ComplexCollectionActivity() {
    override lateinit var adapter: ItemAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = StackAdapter(this, dataModel)
        super.onCreate(savedInstanceState)

        binding.bPop.setOnClickListener {
            val item = adapter.pop() ?: return@setOnClickListener

            Toast.makeText(this@StackActivity, item.name, Toast.LENGTH_SHORT).show()
        }
    }
}
