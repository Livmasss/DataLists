package com.livmas.itertable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.recyclerAdapters.CollectionsAdapter
import com.livmas.itertable.databinding.ActivityMainBinding
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.dataBaseEntities.Colls
import com.livmas.itertable.entities.dialogs.NewCollectionDialogFragment

class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var db: MainDB
    val adapter = CollectionsAdapter(ArrayList())
    var number = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)

        initRecycler()
        initAdapter()
        binding.FAB.setOnClickListener { FABClickListener() }

        setAdapterObserver()
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

        val elem = CollectionItem(number++.toString(), CollectionType.List)
        print(dataModel.collectionType.value)
        adapter.addCollection(elem)
    }

    private fun setAdapterObserver() {
        dataModel.collectionName.observe(this) { name ->
            val id = dataModel.collectionId.value ?: return@observe
            dataModel.collectionType.value?.let {
                    type -> adapter.setItemData(id, name, type) }

            insertDBTread()
        }
    }

    private fun initAdapter() {
        Thread {
            db.getDao().getAllColls().forEach { coll ->
                var type = CollectionType.valueOf(coll.typeId)
                if (type == null) {
                    type = CollectionType.List
                }
                val item = CollectionItem(coll.name, type)
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
            val dbItem = Colls(null,
                dataModel.collectionName.value.orEmpty(),
                type.id)
            db.getDao().insertColl(dbItem)
        }.start()
    }

}