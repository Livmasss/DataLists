package com.livmas.itertable.entities.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.livmas.itertable.entities.CollectionType

@Entity(tableName = "collections")
data class CollectionItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "type_id")
    var type: CollectionType,
)