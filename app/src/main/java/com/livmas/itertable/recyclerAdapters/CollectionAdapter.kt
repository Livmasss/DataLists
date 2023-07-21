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
import com.livmas.itertable.activities.ListActivity
import com.livmas.itertable.databinding.CollectionItemBinding
import com.livmas.itertable.entities.CollectionParcelable
import com.livmas.itertable.entities.items.CollectionItem
import java.util.ArrayList

class CollectionAdapter(private val db: MainDB, private val context: Context,
                        private val dataModel: DataModel):
        RecyclerView.Adapter<CollectionAdapter.CollectionHolder>(),
        Adapter<CollectionItem> {
    private val dataSet = ArrayList<CollectionItem>()

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
            openList(dataSet[position])
        }

        holder.getBinding().root.setOnLongClickListener {
            dataModel.editCollIndex.value = position
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun add(item: CollectionItem) {
        dataSet.add(item)
        notifyItemChanged(dataSet.size - 1)
    }

    fun openList(list: CollectionItem) {
        val intent = Intent(context, ListActivity::class.java)
        intent.putExtra("collection", CollectionParcelable(list.id, list.name, list.type.ordinal))
        startActivity(context, intent, null)
    }

    override fun onDeleteClickListener(position: Int) {
        db.deleteCollection(dataSet[position])
        dataSet.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataSet.size)
    }


//    fun findItem(item: CollectionItem): Int {
//        for (i in 0 until dataSet.size) {
//            if (dataSet[i].number == item.number) {
//                return i
//            }
//        }
//        return -1
//    }

//    fun at(position: Int): CollectionItem {
//        val iter = dataSet.iterator()
//        var index = 0
//        while (iter.hasNext()) {
//            val item = iter.next()
//            if (index == position)
//                return item
//            index++
//        }
//        throw IndexOutOfBoundsException()
//    }
//
//    fun removeAt(position: Int) {
//        val iterator = dataSet.iterator()
//        var index = 0
//        while (iterator.hasNext()) {
//            iterator.next()
//            if (position == index) {
//                iterator.remove()
//                return
//            }
//            index++
//        }
//    }

    fun setItemData(position: Int, name: String) {
        dataSet[position].name = name

        notifyItemChanged(position)
    }
}