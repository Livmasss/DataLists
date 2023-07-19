package com.livmas.itertable.recyclerAdapters

interface Adapter<T> {
    fun add(item: T)
    fun onDeleteClickListener(position: Int)

    interface Holder<T, BT> {
        fun bind(item: T, pos: Int)
        fun getBinding(): BT
    }
}