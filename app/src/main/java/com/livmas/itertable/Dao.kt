package com.livmas.itertable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.livmas.itertable.entities.items.CollectionItem

@Dao
interface Dao {
    @Insert
    fun insertColl(item: CollectionItem): Long
    @Query("SELECT * FROM collections")
    fun getAllColls(): List<CollectionItem>
    @Delete
    fun deleteColl(item: CollectionItem)
}