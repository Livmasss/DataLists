package com.livmas.itertable.recyclerAdapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.DataModel
import com.livmas.itertable.MainDB
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ListItemBinding
import com.livmas.itertable.entities.items.ListItem
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.LinkedList

open class ItemAdapter(protected val dataSet: LinkedList<ListItem>, private val context: Context, val dataModel: DataModel)
    : Adapter<ListItem>,
    RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    val db = MainDB.getDB(context)

    class ItemHolder(view: View) :
        RecyclerView.ViewHolder(view),
        Adapter.Holder<ListItem, ListItemBinding> {

        private val binding = ListItemBinding.bind(view)
        override fun bind(item: ListItem, pos: Int) {
            binding.apply {
                tvName.text = item.name
            }
        }

        override fun getBinding(): ListItemBinding {
            return binding
        }
    }

    override fun add(item: ListItem) {
        dataSet.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun insert(position: Int, item: ListItem) {
        dataSet.add(position, item)
        notifyItemInserted(position)
    }


    override fun onDeleteClickListener(position: Int) {
        db.deleteItem(dataSet[position])
        remove(position)
    }

    override fun remove(position: Int) {
        dataSet.removeAt(position)

        updateRangeNumbers(position, itemCount)
        notifyItemRemoved(position)
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
        holder.getBinding().root.setOnLongClickListener {
            dataModel.editItemIndex.value = position
            return@setOnLongClickListener true
        }
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

    fun dbUpdate() {
        Thread {
            dataSet.forEach {
                db.getDao().updateItem(it)
            }
        }.start()
    }
}
