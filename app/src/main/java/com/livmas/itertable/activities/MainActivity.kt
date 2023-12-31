package com.livmas.itertable.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.livmas.itertable.AlarmController
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.entities.Alarm
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.fragments.EditItemDialog
import com.livmas.itertable.fragments.NewCollectionDialog
import com.livmas.itertable.itemTouchCallbacks.CollectionTouchCallback
import com.livmas.itertable.recyclerAdapters.CollectionAdapter

class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    private lateinit var binding: ActivityCollectionBinding
    private lateinit var db: MainDB
    private lateinit var adapter: CollectionAdapter
    private lateinit var alarmController: AlarmController

    companion object {
        private lateinit var alarms: List<Alarm>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmController = AlarmController(this)
        db = MainDB.getDB(this)
        adapter = CollectionAdapter(this, dataModel)

        initRecycler()
        initAdapter()
        binding.apply {
            tvType.visibility = View.GONE
            bBack.visibility = View.GONE
            fabNewItem.setOnClickListener { newClickListener() }
        }
        Thread{
            fixAlarms()
        }.start()

        setDialogObserver()
    }

    override fun onStop() {
        super.onStop()
        adapter.dbUpdate()
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

        val touchHelper = ItemTouchHelper(CollectionTouchCallback(adapter))
        touchHelper.attachToRecyclerView(binding.rvContent)
    }

    private fun newClickListener() {
        val newCollectionDialog = NewCollectionDialog()
        newCollectionDialog.show(supportFragmentManager, "collection")
    }

    private fun setDialogObserver() {
        lateinit var item: CollectionItem
        dataModel.newCollection.observe(this) { _ ->
            dataModel.newCollection.value?.let { coll ->
                item = coll
                adapter.add(item) }

            Thread {
                val id = db.insertCollectionFromDataModel(dataModel)
                item.id = id.toInt()

                adapter.openCollection(item)
            }.start()
        }

        dataModel.editCollIndex.observe(this) {
            val dialog = EditItemDialog(dataModel.editCollName, adapter.at(it).name)
            dialog.show(supportFragmentManager, "collection")
        }

        dataModel.editCollName.observe(this) { name ->
                val index = dataModel.editCollIndex.value!!
                adapter.setItemData(index, name)
        }
    }

    private fun initAdapter() {
        Thread {
            db.getDao().getAllColls().forEach { coll ->
                adapter.add(coll)
            }
        }.start()
    }

    private fun fixAlarms() {
        alarms = db.getDao().getAllAlarms()

        for(alarm in alarms) {
            val collection = adapter.findById(alarm.collectionId) ?: return

            if (alarm.repeat > (0).toLong())
                alarm.lastCall += alarm.repeat

            alarmController.create(alarm, collection)
        }
    }
}