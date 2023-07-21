package com.livmas.itertable.recyclerAdapters

import android.annotation.SuppressLint
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

class ListAdapter(private val dataSet: ArrayList<ListItem>, private val context: Context, private val dataModel: DataModel):
        RecyclerView.Adapter<ListAdapter.ListHolder>(),
        Adapter<ListItem> {
    private val db = MainDB.getDB(context)
    class ListHolder(view: View):
            RecyclerView.ViewHolder(view),
            Adapter.Holder<ListItem, ListItemBinding> {
        private val binding = ListItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        override fun bind(item: ListItem, pos: Int) {
            binding.apply {
                tvNumber.text = (pos + 1).toString()
                tvName.text = item.name
            }
        }

        override fun getBinding(): ListItemBinding {
            return binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
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

    override fun add(item: ListItem) {
        dataSet.add(item)
        notifyItemChanged(itemCount - 1)
    }

    override fun onDeleteClickListener(position: Int) {
        db.deleteItem(dataSet[position])
        dataSet.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun setItemData(position: Int, name: String) {
        dataSet[position].name = name

        notifyItemChanged(position)
    }
}