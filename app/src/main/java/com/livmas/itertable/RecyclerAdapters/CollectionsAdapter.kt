package com.livmas.itertable.RecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.R
import com.livmas.itertable.databinding.CollectionItemBinding
import com.livmas.itertable.entities.CollectionItem

class CollectionsAdapter: RecyclerView.Adapter<CollectionsAdapter.CollectionHolder>() {
    private val dataSet = ArrayList<CollectionItem>()
    class CollectionHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = CollectionItemBinding.bind(item)
        fun bind(elem: CollectionItem) {
            binding.apply {
                nameTextView.text = elem.name
                typeTextView.text = elem.type.toString()
                numberTextView.text = elem.number.toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.collection_item, parent, false)
        return CollectionHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addCollection(elem: CollectionItem) {
        elem.number = itemCount + 1

        dataSet.add(elem)
        notifyItemChanged(itemCount - 1)
    }
}