package com.livmas.itertable.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.entities.CollectionType
import com.livmas.itertable.entities.ListItem
import com.livmas.itertable.fragments.EditItemDialog
import com.livmas.itertable.fragments.NewListDialog
import com.livmas.itertable.itemTouchCallbacks.ItemTouchCallback
import com.livmas.itertable.recyclerAdapters.ItemAdapter

abstract class CollectionActivity: AppCompatActivity() {
    companion object {
        var activeCollInfo: CollectionItem? = null
    }

    protected val dataModel: DataModel by viewModels()
    protected lateinit var db: MainDB
    protected abstract var adapter: ItemAdapter
    lateinit var collInfo: CollectionItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = MainDB.getDB(this)
        collInfo = intent.getParcelableExtra("collection", CollectionItem::class.java)!!

        setObservers()
    }

    override fun onStart() {
        super.onStart()
        activeCollInfo = collInfo
    }

    override fun onResume() {
        super.onResume()
        initList()
    }

    override fun onStop() {
        super.onStop()
        activeCollInfo = null
        adapter.dbUpdate()
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

    private fun initList() {
        Thread {
            adapter.clear()
            val data = db.getDao().getCollItems(collInfo.id!!)
            data.forEach { item ->
                adapter.apply {
                    add(item)
                }
            }
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    protected fun startNewListDialog() {
        val dialog = NewListDialog()
        dialog.show(supportFragmentManager, "list")
    }

    open fun setObservers() {
        dataModel.newListName.observe(this) { name ->
            val item = collInfo.id?.let { masterId ->
                if (collInfo.type == CollectionType.CheckList)
                    ListItem(null, name, masterId, adapter.itemCount + 1, false)
                else
                    ListItem(null, name, masterId, adapter.itemCount + 1, null)
            }

            if (item != null) {
                adapter.apply {
                    notifiedAdd(item)
                }
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