package com.livmas.itertable.activities.collectionActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.livmas.itertable.recyclerAdapters.collections.QueueAdapter

open class QueueActivity : AppCompatActivity() {
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
            bPop.setOnClickListener {
                val item = adapter.pop()
                Toast.makeText(this@QueueActivity, item.name, Toast.LENGTH_SHORT).show()
            }
        }

        initRecycler()
        initList()

        setObservers()
    }


    private fun startNewListDialog() {
        val dialog = NewListDialog()
        dialog.show(supportFragmentManager, "list")
    }

    private fun setObservers() {
        dataModel.newListName.observe(this) { name ->
            val item = collInfo.id?.let { masterId ->
                ListItem(null, name, masterId, adapter.itemCount + 1)
            }

            if (item != null) {
                adapter.add(item)
                Thread {
                    val id = db.getDao().insertItem(item)
                    item.id = id.toInt()
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
            val data = db.getDao().getCollItems(collInfo.id!!)
            data.forEach { item ->
                if (item.number == 0) {
                    item.number = adapter.itemCount + 1
                    Thread {
                        db.getDao().updateItem(item)
                    }.start()
                }
                adapter.add(item)
            }
        }.start()
    }
}