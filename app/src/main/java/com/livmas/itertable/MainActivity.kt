package com.livmas.itertable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
        binding.FAB.setOnClickListener { FABClickListener() }

        setObserver()
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

    private fun setObserver() {
        dataModel.collectionName.observe(this) { name ->
            val id = dataModel.collectionId.value ?: return@observe
            dataModel.collectionType.value?.let {
                    type -> adapter.setItemData(id, name, type) }

            insertDBTread()
        }
    }

    private fun insertDBTread() {
        Thread {
            val dbItem = Colls(null,
                dataModel.collectionName.value.orEmpty(),
                dataModel.collectionType.value.toInt())
            db.getDao().insertColl(dbItem)
        }.start()
    }

    inline fun <reified T: Enum<T>> T?.toInt(): Int {
        return this?.ordinal ?: 0
    }
}