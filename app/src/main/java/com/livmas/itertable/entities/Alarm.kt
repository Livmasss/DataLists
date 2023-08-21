package com.livmas.itertable.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey
    val collectionId: Int,
    @ColumnInfo("last_call")
    val lastCall: Long,
    @ColumnInfo("repeat")
    val repeat: Long,
    @ColumnInfo("is_active", defaultValue = false.toString())
    val isActive: Boolean
)