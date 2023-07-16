package com.livmas.itertable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.recyclerAdapters.CollectionsAdapter
import com.livmas.itertable.databinding.ActivityMainBinding
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.entities.dialogs.NewCollectionDialogFragment

class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var db: MainDB
    lateinit var adapter: CollectionsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)
        adapter = CollectionsAdapter(ArrayList(), db, this)

        initRecycler()
        initAdapter()
        binding.FAB.setOnClickListener { FABClickListener() }

        setDialogObserver()
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun FABClickListener() {
        dataModel.collectionId.value = adapter.itemCount

        val newCollectionDialogFragment = NewCollectionDialogFragment()
        newCollectionDialogFragment.show(supportFragmentManager, "collection")
//
//        val elem = CollectionItem(-1, "My collection", CollectionType.List)
//        print(dataModel.collectionType.value)
    }

    private fun setDialogObserver() {
        dataModel.collectionName.observe(this) { name ->
            dataModel.collectionType.value?.let { type ->
                val item = CollectionItem(null, name, type)
                adapter.addCollection(item) }

            db.insertThread(dataModel)
        }
    }

    private fun initAdapter() {
        Thread {
            db.getDao().getAllColls().forEach { coll ->
                adapter.addCollection(coll)
            }
        }.start()
    }
}