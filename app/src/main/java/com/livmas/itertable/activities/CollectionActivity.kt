package com.livmas.itertable.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.DataModel
import com.livmas.itertable.ItemTouchCallback
import com.livmas.itertable.MainDB
import com.livmas.itertable.dialogs.EditItemDialog
import com.livmas.itertable.dialogs.NewListDialog
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.items.ListItem
import com.livmas.itertable.recyclerAdapters.ItemAdapter

abstract class CollectionActivity: AppCompatActivity() {

    protected val dataModel: DataModel by viewModels()
    private lateinit var db: MainDB
    protected abstract var adapter: ItemAdapter
    protected lateinit var collInfo: CollectionParcelable

    override fun onStop() {
        super.onStop()
        adapter.dbUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = MainDB.getDB(this)
        collInfo = intent.getParcelableExtra("collection")!!

        initList()
        setObservers()
    }

    protected fun initRecycler(rv: RecyclerView) {
        val context = this
        with(rv) {
            layoutManager = LinearLayoutManager(context)
            adapter = context.adapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL)
            )
        }

        val touchHelper = ItemTouchHelper(ItemTouchCallback(adapter))
        touchHelper.attachToRecyclerView(rv)
    }

    protected fun initList() {
        Thread {
            val data = db.getDao().getCollItems(collInfo.id!!)
            data.forEach { item ->
                adapter.add(item)
            }
        }.start()
    }

    protected fun startNewListDialog() {
        val dialog = NewListDialog()
        dialog.show(supportFragmentManager, "list")
    }

    protected fun setObservers() {
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
        }
    }
}