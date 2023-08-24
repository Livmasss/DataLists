package com.livmas.itertable.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey
    val collectionId: Int,
    @ColumnInfo("last_call")
    var lastCall: Long,
    @ColumnInfo("repeat")
    val repeat: Long,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(collectionId)
        parcel.writeLong(lastCall)
        parcel.writeLong(repeat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alarm> {
        override fun createFromParcel(parcel: Parcel): Alarm {
            return Alarm(parcel)
        }

        override fun newArray(size: Int): Array<Alarm?> {
            return arrayOfNulls(size)
        }
    }
}