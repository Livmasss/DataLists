package com.livmas.itertable.recyclerAdapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.R
import com.livmas.itertable.activities.MainActivity
import com.livmas.itertable.databinding.ListItemBinding
import com.livmas.itertable.entities.items.ListItem

class ListAdapter(private val dataSet: ArrayList<ListItem>):
        RecyclerView.Adapter<ListAdapter.ListHolder>(),
        Adapter<ListItem> {
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
    }

    override fun add(item: ListItem) {
        dataSet.add(item)
        notifyItemChanged(itemCount - 1)
    }

    override fun onDeleteClickListener(position: Int) {
        TODO("Not yet implemented")
    }
}