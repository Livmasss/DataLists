package com.livmas.itertable.entities.dataBaseEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "items",
    foreignKeys = [ForeignKey(
        entity = Colls::class,
        parentColumns = ["id"],
        childColumns = ["collection_id"]
    )])
data class CollElems(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "collection_id")
    var collId: Int,
    @ColumnInfo(name = "value")
    var value: String
)
