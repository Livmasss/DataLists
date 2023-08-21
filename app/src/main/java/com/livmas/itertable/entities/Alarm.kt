package com.livmas.itertable.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "collection_id")
    val collectionId: Int,
    @ColumnInfo("last_call")
    val lastCall: Long,
    @ColumnInfo("repeat")
    val repeat: Long
)