package com.livmas.itertable.recyclerAdapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livmas.itertable.MainDB
import com.livmas.itertable.R
import com.livmas.itertable.databinding.CollectionItemBinding
import com.livmas.itertable.entities.items.CollectionItem

class CollectionsAdapter(private val dataSet: ArrayList<CollectionItem>, private val db: MainDB, private val context: Context):
    RecyclerView.Adapter<CollectionsAdapter.CollectionHolder>() {

    class CollectionHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = CollectionItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(elem: CollectionItem, pos: Int) {
            binding.apply {
                nameTextView.text = elem.name
                typeTextView.text = elem.type.toString()
                numberTextView.text = (pos + 1).toString()
            }
        }
        fun getBinding(): CollectionItemBinding {
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
                    db.deleteThread(dataSet[position])
                    dataSet.removeAt(position)

                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, dataSet.size)
                }
                .create()


            deleteDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addCollection(elem: CollectionItem) {
        dataSet.add(elem)
        notifyItemChanged(dataSet.size - 1)
    }

//    fun findItem(item: CollectionItem): Int {
//        for (i in 0 until dataSet.size) {
//            if (dataSet[i].id == item.id) {
//                return i
//            }
//        }
//        return -1
//    }
//
//    fun at(id: Int): CollectionItem {
//        return dataSet[id]
//    }

//    fun setItemData(id: Int, name: String, type: CollectionType, ) {
//        dataSet[id].name = name
//        dataSet[id].type = type
//
//        notifyItemChanged(id)
//    }
}