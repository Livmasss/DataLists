package com.livmas.itertable.recyclerAdapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.R
import com.livmas.itertable.activities.collectionActivities.CheckedListActivity
import com.livmas.itertable.activities.collectionActivities.CycleActivity
import com.livmas.itertable.activities.collectionActivities.ListActivity
import com.livmas.itertable.activities.collectionActivities.QueueActivity
import com.livmas.itertable.activities.collectionActivities.StackActivity
import com.livmas.itertable.databinding.CollectionItemBinding
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.entities.CollectionType
import java.util.Collections

class CollectionAdapter(private val context: Context, private val dataModel: DataModel):
        RecyclerView.Adapter<CollectionAdapter.CollectionHolder>(),
        Adapter<CollectionItem> {
    private val dataSet = ArrayList<CollectionItem>()
    private val db = MainDB.getDB(context)

    class CollectionHolder(view: View):
            RecyclerView.ViewHolder(view),
            Adapter.Holder<CollectionItem, CollectionItemBinding> {
        private val binding = CollectionItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        override fun bind(item: CollectionItem, pos: Int) {
            binding.apply {
                tvName.text = item.name
                tvType.text = item.type.toString()
                tvNumber.text = (pos + 1).toString()
            }
        }
        override fun getBinding(): CollectionItemBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.collection_item, parent, false)
        return CollectionHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        holder.bind(dataSet[position], position)
        holder.getBinding().ibDelete.setOnClickListener {

            val deleteDialog = AlertDialog.Builder(context)
                .setMessage(R.string.delete_message)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    onDeleteClickListener(position)
                }
                .create()

            deleteDialog.show()
        }

        holder.getBinding().root.setOnClickListener {
            openCollection(dataSet[position])
        }

        holder.getBinding().ibEdit.setOnClickListener {
            dataModel.editCollIndex.value = position
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun add(item: CollectionItem) {
        dataSet.add(item)
    }

    fun swap(from: Int, to: Int) {
        Collections.swap(dataSet, from, to)

        val min = Integer.min(from, to)
        val max = Integer.max(from, to)

        updateRangeNumbers(min, max+1)
        notifyItemMoved(from, to)
    }

    override fun setItemData(position: Int, name: String) {
        dataSet[position].name = name

        notifyItemChanged(position)
    }

    fun openCollection(coll: CollectionItem) {
        val intent = Intent(context, when (coll.type) {
            CollectionType.List -> ListActivity::class.java
            CollectionType.Queue -> QueueActivity::class.java
            CollectionType.Cycle -> CycleActivity::class.java
            CollectionType.Stack -> StackActivity::class.java
            CollectionType.CheckList -> CheckedListActivity::class.java
        }
            )
        intent.putExtra("collection", CollectionItem(coll.id, coll.name, coll.type, coll.number))
        startActivity(context, intent, null)
    }

    override fun notifiedAdd(item: CollectionItem) {
        add(item)
        notifyItemInserted(itemCount - 1)
    }

    override fun onDeleteClickListener(position: Int) {
        db.deleteCollection(dataSet[position])
        notifiedRemove(position)
    }

    override fun remove(position: Int) {
        dataSet.removeAt(position)
    }

    override fun notifiedRemove(position: Int) {
        remove(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataSet.size)
    }

    override fun at(position: Int): CollectionItem {
        return dataSet[position]
    }

    fun dbUpdate() {
        Thread {
            dataSet.forEach {
                db.getDao().updateColl(it)
            }
        }.start()
    }
    fun updateNumber(position: Int) {
        dataSet[position].number = position + 1
    }
    fun updateRangeNumbers(start: Int, end: Int) {
        for (i in start until end)
            updateNumber(i)
    }
    fun updateNumbers() {
        updateRangeNumbers(0, itemCount)
    }

    fun findById(id: Int): CollectionItem? {
        return dataSet.find {
            return@find (it.id == id)
        }
    }
}