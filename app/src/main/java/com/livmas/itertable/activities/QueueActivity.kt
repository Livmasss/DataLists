package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityComplexCollectionBinding
import com.livmas.itertable.dialogs.EditItemDialog
import com.livmas.itertable.dialogs.NewListDialog
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.QueueAdapter

class QueueActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    private lateinit var binding: ActivityComplexCollectionBinding
    private lateinit var db: MainDB
    private lateinit var adapter: QueueAdapter
    private lateinit var collInfo: CollectionParcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityComplexCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)
        adapter = QueueAdapter(this, dataModel)
        collInfo = intent.getParcelableExtra("collection")!!

        binding.apply {
            tvTitle.text = collInfo.name + ":"
            tvType.text = CollectionType.valueOf(collInfo.typeId).toString()

            fabNewItem.setOnClickListener {
                startNewListDialog()
            }
            bBack.setOnClickListener {
                finish()
            }
        }

        initRecycler()
        initList()

        setObservers()
    }


    fun startNewListDialog() {
        val dialog = NewListDialog()
        dialog.show(supportFragmentManager, "list")
    }

    fun setObservers() {
        dataModel.newListName.observe(this) { name ->
            val item = collInfo.id?.let { masterId ->
                ListItem(null, name, masterId)
            }

            if (item != null) {
                adapter.add(item)
                Thread {
                    db.getDao().insertItem(item)
                }.start()
            }
        }

        dataModel.editItemIndex.observe(this) {
            val dialog = EditItemDialog(dataModel.editItemName, adapter.at(it).name)
            dialog.show(supportFragmentManager, "item")
        }

        dataModel.editItemName.observe(this) { name ->
            val index = dataModel.editItemIndex.value!!
            adapter.setItemData(index, name)
            Thread{
                db.getDao().updateItem(adapter.at(index))
            }.start()
        }
    }

    private fun initRecycler() {
        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(this@QueueActivity)
            adapter = this@QueueActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@QueueActivity,
                    LinearLayoutManager.VERTICAL)
            )
        }
    }

    private fun initList() {
        Thread {
            val data = db.getDao().getCollectionItems(collInfo.id!!)
            data.forEach { item ->
                adapter.add(item)
            }
        }.start()
    }
}