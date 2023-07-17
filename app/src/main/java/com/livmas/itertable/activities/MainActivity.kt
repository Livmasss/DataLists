package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.recyclerAdapters.CollectionsAdapter
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.dialogs.NewCollectionDialogFragment

class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    private lateinit var binding: ActivityCollectionBinding
    lateinit var db: MainDB
    lateinit var adapter: CollectionsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)
        adapter = CollectionsAdapter(ArrayList(), db, this)

        initRecycler()
        initAdapter()
        binding.fbNewItem.setOnClickListener { FABClickListener() }

        setDialogObserver()
    }

    private fun initRecycler() {
        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun FABClickListener() {
        dataModel.collectionNumber.value = adapter.itemCount

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