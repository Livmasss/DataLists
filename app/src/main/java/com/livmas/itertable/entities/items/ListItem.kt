package com.livmas.itertable.entities.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "master_id")
    val masterId: Int
)
