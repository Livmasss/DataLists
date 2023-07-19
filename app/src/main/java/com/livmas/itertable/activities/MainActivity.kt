package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.recyclerAdapters.CollectionAdapter
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.dialogs.NewCollectionDialog

class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    private lateinit var binding: ActivityCollectionBinding
    lateinit var db: MainDB
    lateinit var adapter: CollectionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)
        adapter = CollectionAdapter(db, this)

        initRecycler()
        initAdapter()
        binding.apply {
            tvType.visibility = View.GONE
            bBack.visibility = View.GONE
            fbNewItem.setOnClickListener { FABClickListener() }
        }


        setDialogObserver()
    }

    private fun initRecycler() {
        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun FABClickListener() {
        dataModel.collNumber.value = adapter.itemCount

        val newCollectionDialog = NewCollectionDialog()
        newCollectionDialog.show(supportFragmentManager, "collection")
//
//        val elem = CollectionItem(-1, "My collection", CollectionType.List)
//        print(dataModel.collectionType.value)
    }

    private fun setDialogObserver() {
        dataModel.collName.observe(this) { name ->
            dataModel.collType.value?.let { type ->
                val item = CollectionItem(null, name, type)
                adapter.add(item) }

            db.insertCollectionFromDataModel(dataModel)

        }
    }

    private fun initAdapter() {
        Thread {
            db.getDao().getAllColls().forEach { coll ->
                adapter.add(coll)
            }
        }.start()
    }
}