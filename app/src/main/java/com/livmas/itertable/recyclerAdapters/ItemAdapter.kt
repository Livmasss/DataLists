package com.livmas.itertable.recyclerAdapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ListItemBinding
import com.livmas.itertable.entities.ListItem
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.Collections
import java.util.LinkedList

abstract class ItemAdapter(protected val dataSet: LinkedList<ListItem>, private val context: Context, val dataModel: DataModel)
    : Adapter<ListItem>,
    RecyclerView.Adapter<ItemAdapter.ItemHolder>(){

    val db = MainDB.getDB(context)

    class ItemHolder(view: View) :
        RecyclerView.ViewHolder(view),
        Adapter.Holder<ListItem, ListItemBinding> {

        private val binding = ListItemBinding.bind(view)
        override fun bind(item: ListItem, pos: Int) {
            binding.apply {
                tvNumber.text = item.number.toString()
                tvName.text = item.name
            }
        }

        override fun getBinding(): ListItemBinding {
            return binding
        }
    }

    override fun add(item: ListItem) {
        dataSet.add(item)
    }

    override fun notifiedAdd(item: ListItem) {
        add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun insert(position: Int, item: ListItem) {
        dataSet.add(position, item)
        notifyItemInserted(position)
    }

    abstract fun pop(): ListItem?
    abstract fun getItem(): ListItem?

    override fun onDeleteClickListener(position: Int) {
        db.deleteItem(dataSet[position])
        notifiedRemove(position)
    }

    override fun remove(position: Int) {
        dataSet.removeAt(position)
    }

    override fun notifiedRemove(position: Int) {
        remove(position)

        notifyItemRemoved(position)
        updateRangeNumbers(position, itemCount)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun setItemData(position: Int, name: String) {
        dataSet[position].name = name
        notifyItemChanged(position)
    }

    override fun at(position: Int): ListItem {
        return dataSet[position]
    }

    fun swap(from: Int, to: Int) {
        Collections.swap(dataSet, from, to)

        val min = min(from, to)
        val max = max(from, to)

        updateRangeNumbers(min, max+1)
        notifyItemMoved(from, to)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(dataSet[position], position)
        holder.getBinding().ibDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                .setMessage(R.string.delete_message)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    onDeleteClickListener(position)
                }
                .create()

            dialog.show()
        }
        holder.getBinding().ibEdit.setOnClickListener {
            dataModel.editItemIndex.value = position
        }
    }

    open fun updateNumber(position: Int) {
        dataSet[position].number = position + 1
    }
    fun updateRangeNumbers(start: Int, end: Int) {
        for (i in start until end)
            updateNumber(i)
    }
    fun updateNumbers() {
        updateRangeNumbers(0, itemCount)
    }

    fun dbUpdate() {
        Thread {
            dataSet.forEach {
                db.getDao().updateItem(it)
            }
        }.start()
    }

    protected fun isEmpty(): Boolean {
        if (itemCount < 1) {
            Toast.makeText(context, R.string.empty_collection, Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}
