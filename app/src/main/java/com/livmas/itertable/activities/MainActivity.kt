package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.dialogs.EditItemDialog
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
        adapter = CollectionAdapter(db, this, dataModel)

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
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL)
            )
        }
    }

    private fun FABClickListener() {
        val newCollectionDialog = NewCollectionDialog()
        newCollectionDialog.show(supportFragmentManager, "collection")
    }

    private fun setDialogObserver() {
        lateinit var item: CollectionItem
        dataModel.newCollName.observe(this) { name ->
            dataModel.newCollType.value?.let { type ->
                item = CollectionItem(null, name, type)
                adapter.add(item) }

            Thread {
                val id = db.insertCollectionFromDataModel(dataModel)
                item.id = id.toInt()

                adapter.openList(item)
            }.start()
        }

        dataModel.editCollIndex.observe(this) {
            val dialog = EditItemDialog(dataModel.editCollName)
            dialog.show(supportFragmentManager, "collection")
        }

        dataModel.editCollName.observe(this) { name ->
            adapter.setItemData(
                dataModel.editCollIndex.value!!,
                name
            )
        }
    }

    private fun initAdapter() {
        Thread {
            db.getDao().getAllColls().forEach { coll ->
                adapter.add(coll)
            }
        }.start()
    }

    fun getSupFragmentManager(): FragmentManager {
        return supportFragmentManager
    }
}