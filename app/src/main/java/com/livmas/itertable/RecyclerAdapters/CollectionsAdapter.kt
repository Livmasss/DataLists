package com.livmas.itertable.RecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.R
import com.livmas.itertable.databinding.CollectionElementBinding
import com.livmas.itertable.entities.CollectionElement
import kotlin.collections.ArrayList

class CollectionsAdapter: RecyclerView.Adapter<CollectionsAdapter.CollectionHolder>() {
    val dataSet = ArrayList<CollectionElement>()
    class CollectionHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = CollectionElementBinding.bind(item)
        fun bind(elem: CollectionElement) {
            binding.apply {
                nameTextView.text = elem.name
                typeTextView.text = elem.type.toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.collection_element, parent, false)
        return CollectionHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addCollection(elem: CollectionElement) {
        dataSet.add(elem)
        notifyItemChanged(itemCount - 1)
    }
}