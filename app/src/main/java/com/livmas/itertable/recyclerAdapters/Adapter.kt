package com.livmas.itertable.recyclerAdapters

interface Adapter<T> {
    fun add(item: T)
    fun onDeleteClickListener(position: Int)
    fun remove(position: Int)
    fun setItemData(position: Int, name: String)
    fun at(position: Int): T

    interface Holder<T, BT> {
        fun bind(item: T, pos: Int)
        fun getBinding(): BT
    }
}