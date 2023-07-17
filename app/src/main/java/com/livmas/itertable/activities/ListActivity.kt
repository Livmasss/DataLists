package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.ListAdapter
import java.util.ArrayList

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCollectionBinding
    private lateinit var db: MainDB
    private lateinit var adapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter(ArrayList<ListItem>())
    }
}