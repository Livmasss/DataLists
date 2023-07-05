package com.livmas.itertable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.RecyclerAdapters.CollectionsAdapter
import com.livmas.itertable.databinding.ActivityMainBinding
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.entities.CollectionType

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val adapter = CollectionsAdapter()
    var number = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()
        binding.FAB.setOnClickListener { FABClickListener() }
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

    }

    private fun FABClickListener() {
        val elem = CollectionItem(number++.toString(), CollectionType.List)
        adapter.addCollection(elem)
    }
}