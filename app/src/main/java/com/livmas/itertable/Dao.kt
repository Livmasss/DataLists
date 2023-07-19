package com.livmas.itertable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.livmas.itertable.entities.items.CollectionItem
import com.livmas.itertable.entities.items.ListItem

@Dao
interface Dao {
    @Insert
    fun insertColl(item: CollectionItem): Long
    @Insert
    fun insertItem(item: ListItem)
    @Query("SELECT * FROM collections")
    fun getAllColls(): List<CollectionItem>
    @Query("SELECT * FROM items WHERE master_id = :id")
    fun getCollectionItems(id: Int): List<ListItem>
    @Delete
    fun deleteColl(item: CollectionItem)
    @Delete
    fun deleteItem(item: ListItem)
}