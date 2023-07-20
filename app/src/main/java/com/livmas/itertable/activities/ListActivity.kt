package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.dialogs.NewListDialog
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.ListAdapter
import java.util.ArrayList


class ListActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    private lateinit var binding: ActivityCollectionBinding
    private lateinit var db: MainDB
    private lateinit var adapter: ListAdapter
    private lateinit var collInfo: CollectionParcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDB.getDB(this)
        adapter = ListAdapter(ArrayList<ListItem>(), this)
        collInfo = intent.getParcelableExtra("collection")!!

        binding.apply {
            tvTitle.text = collInfo.name + ":"
            tvType.text = CollectionType.valueOf(collInfo.typeId).toString()

            fbNewItem.setOnClickListener {
                startNewListDialog()
            }
            bBack.setOnClickListener {
                finish()
            }
        }

        initRecycler()
        initList()

        setNewListDialogObserver()
    }

    private fun initRecycler() {
        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(this@ListActivity)
            adapter = this@ListActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@ListActivity,
                    LinearLayoutManager.VERTICAL)
            )
        }
    }

    private fun startNewListDialog() {
        val dialog = NewListDialog()
        dialog.show(supportFragmentManager, "list")
    }

    private fun setNewListDialogObserver() {
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