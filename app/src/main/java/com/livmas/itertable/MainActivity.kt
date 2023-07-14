package com.livmas.itertable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.recyclerAdapters.CollectionsAdapter
import com.livmas.itertable.databinding.ActivityMainBinding
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.dialogs.NewCollectionDialogFragment

class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var db: MainDB
    val adapter = CollectionsAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)

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

            insertDBTread()
        }
    }

    private fun initAdapter() {
        Thread {
            db.getDao().getAllColls().forEach { coll ->
                val item = CollectionItem(coll.id, coll.name, coll.type)
                adapter.addCollection(item)
            }
        }.start()
    }

    private fun insertDBTread() {
        Thread {
            var type = dataModel.collectionType.value
            if (type == null) {
                type = CollectionType.List
            }
            val item = CollectionItem(null,
                dataModel.collectionName.value.orEmpty(),
                type)
            db.getDao().insertColl(item)
        }.start()
    }

    private fun deleteDBThread(collection: CollectionItem) {
        Thread {
            db.getDao().deleteColl(collection)
        }.start()
    }
}