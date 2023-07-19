package com.livmas.itertable.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
        adapter = ListAdapter(ArrayList<ListItem>())
        collInfo = intent.getParcelableExtra("collection")!!

        binding.tvTitle.text = collInfo.name + ":"
        binding.tvType.text = CollectionType.valueOf(collInfo.typeId).toString()
        binding.fbNewItem.setOnClickListener {
            startNewListDialog()
        }

        initRecycler()

        setNewListDialogObserver()
    }

    private fun initRecycler() {
        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(this@ListActivity)
            adapter = this@ListActivity.adapter

        }
    }

    private fun startNewListDialog() {
        val dialog = NewListDialog()
        dialog.show(supportFragmentManager, "list")
    }

    private fun setNewListDialogObserver() {
        dataModel.listName.observe(this) {name ->
            val item = collInfo.id?.let { masterId ->
                ListItem(null, name, masterId)
            }

            if (item != null) {
                adapter.add(item)
            }
        }
    }
}