package com.livmas.itertable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.livmas.itertable.entities.Alarm
import com.livmas.itertable.entities.CollectionItem
import com.livmas.itertable.entities.ListItem

@Dao
interface Dao {
    @Insert
    fun insertColl(item: CollectionItem): Long
    @Insert
    fun insertItem(item: ListItem): Long
    @Query("SELECT * FROM collections ORDER BY number")
    fun getAllColls(): List<CollectionItem>
    @Query("SELECT * FROM items WHERE master_id = :id ORDER BY number")
    fun getCollItems(id: Int): List<ListItem>
    @Delete
    fun deleteColl(item: CollectionItem)
    @Delete
    fun deleteItem(item: ListItem)
    @Update
    fun updateColl(item: CollectionItem)
    @Update
    fun updateItem(item: ListItem)
    @Insert
    fun insertAlarm(alarm: Alarm)
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): List<Alarm>
    @Update
    fun updateAlarm(alarm: Alarm)
}